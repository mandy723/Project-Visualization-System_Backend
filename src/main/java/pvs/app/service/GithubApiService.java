package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.dto.GithubCommentDTO;
import pvs.app.dto.GithubIssueDTO;
import pvs.app.dto.GithubPullRequestDTO;
import pvs.app.service.thread.GithubCommitLoaderThread;
import pvs.app.service.thread.GithubIssueLoaderThread;
import pvs.app.service.thread.GithubPullRequestLoaderThread;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@SuppressWarnings("squid:S1192")
public class GithubApiService {

    private final WebClient webClientGraphQl;
    private final WebClient webClientRestAPI;
    private Map<String, Object> graphQlQuery;
    private final GithubCommitService githubCommitService;
    private final  GithubCommentService githubCommentService;

    public GithubApiService(WebClient.Builder webClientBuilder, @Value("${webClient.baseUrl.github}") String baseUrl, GithubCommitService githubCommitService, GithubCommentService githubCommentService) {
        String token = System.getenv("PVS_GITHUB_TOKEN");
        this.githubCommitService = githubCommitService;
        this.githubCommentService = githubCommentService;
        this.webClientGraphQl = webClientBuilder.baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
        this.webClientRestAPI = WebClient.builder().baseUrl("https://api.github.com/repos")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    private String dateToISO8601(Date date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    private void setGraphQlGetCommitsTotalCountAndCursorQuery(String owner, String name, Date lastUpdate) {
        String since = dateToISO8601(lastUpdate);
        Map<String, Object> graphQl = new HashMap<>();
        graphQl.put("query", "{repository(owner: \"" + owner + "\", name:\"" + name + "\") {" +
                            "defaultBranchRef {" +
                                "target {" +
                                    "... on Commit {" +
                                        "history (since: \"" + since + "\") {" +
                                            "totalCount\n" +
                                            "pageInfo {" +
                                                "startCursor" +
                                            "}" +
                                        "}" +
                                    "}" +
                                "}" +
                            "}" +
                        "}}");
        this.graphQlQuery = graphQl;
    }

    private void setGraphQlGetIssuesTotalCountQuery(String owner, String name) {
        Map<String, Object> graphQl = new HashMap<>();
        graphQl.put("query", "{repository(owner: \"" + owner + "\", name:\"" + name + "\") {" +
                                "issues {" +
                                    "totalCount" +
                                "}" +
                            "}}");
        this.graphQlQuery = graphQl;
    }

    private void setGraphQlGetPullRequestTotalCountQuery(String owner, String name) {
        Map<String, Object> graphQl = new HashMap<>();
        graphQl.put("query", "{repository(owner: \"" + owner + "\", name:\"" + name + "\") {" +
                "pullRequests {" +
                    "totalCount" +
                    "}" +
                "}}");
        this.graphQlQuery = graphQl;
    }

    private void setGraphQlGetAvatarQuery(String owner) {
        Map<String, Object> graphQl = new HashMap<>();
        graphQl.put("query", "{search(type: USER, query: \"in:username " + owner + "\", first: 1) {" +
                    "edges {" +
                        "node {" +
                            "... on User {" +
                                "avatarUrl" +
                            "}" +
                            "... on Organization {" +
                                "avatarUrl" +
                            "}" +
                        "}" +
                    "}}}");

        this.graphQlQuery = graphQl;
    }

    public boolean getCommitsFromGithub(String owner, String name, Date lastUpdate) throws InterruptedException, IOException {
        this.setGraphQlGetCommitsTotalCountAndCursorQuery(owner, name, lastUpdate);

        String responseJson = Objects.requireNonNull(this.webClientGraphQl.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> paginationInfo = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("repository"))
                .map(repo -> repo.get("defaultBranchRef"))
                .map(branch -> branch.get("target"))
                .map(tag -> tag.get("history"));

        if(paginationInfo.isPresent()) {
            int totalCount = paginationInfo.get().get("totalCount").asInt();

            List<GithubCommitLoaderThread> githubCommitLoaderThreadList = new ArrayList<>();

            if (totalCount != 0) {
                String cursor = paginationInfo.get().get("pageInfo").get("startCursor").textValue()
                        .split(" ")[0];
                int last = 100;
                for (int i = 1; i <= totalCount/100; i++) {
                    GithubCommitLoaderThread githubCommitLoaderThread =
                            new GithubCommitLoaderThread(
                                    this.webClientGraphQl,
                                    this.githubCommitService,
                                    owner,
                                    name,
                                    last,
                                    cursor + " " + (i*100));
                    githubCommitLoaderThreadList.add(githubCommitLoaderThread);
                    githubCommitLoaderThread.start();
                }
                if(totalCount % 100 != 0) {
                    last = (totalCount - 1) % 100;

                    GithubCommitLoaderThread githubCommitLoaderThread =
                            new GithubCommitLoaderThread(
                                    this.webClientGraphQl,
                                    this.githubCommitService,
                                    owner,
                                    name,
                                    last,
                                    cursor + " " + (totalCount - 1));
                    githubCommitLoaderThreadList.add(githubCommitLoaderThread);
                    githubCommitLoaderThread.start();
                }

                for (GithubCommitLoaderThread thread: githubCommitLoaderThreadList) {
                    thread.join();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public List<GithubIssueDTO> getIssuesFromGithub(String owner, String name) throws IOException, InterruptedException  {
        List<GithubIssueDTO> githubIssueDTOList = new ArrayList<>();
        this.setGraphQlGetIssuesTotalCountQuery(owner, name);

        String responseJson = Objects.requireNonNull(this.webClientGraphQl.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> paginationInfo = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("repository"))
                .map(repo -> repo.get("issues"));

        if(paginationInfo.isPresent()) {
            double totalCount = paginationInfo.get().get("totalCount").asInt();
            List<GithubIssueLoaderThread> githubIssueLoaderThreadList = new ArrayList<>();

            if (0 != totalCount) {
                for (int i = 1; i <= Math.ceil(totalCount/100); i++) {
                    GithubIssueLoaderThread githubIssueLoaderThread =
                            new GithubIssueLoaderThread(
                                    githubIssueDTOList,
                                    owner,
                                    name,
                                    i);
                    githubIssueLoaderThreadList.add(githubIssueLoaderThread);
                    githubIssueLoaderThread.start();
                }

                for (GithubIssueLoaderThread thread: githubIssueLoaderThreadList) {
                    thread.join();
                }
            }
        } else {
            return null;
        }
        return githubIssueDTOList;
    }

    public List<GithubPullRequestDTO> getPullRequestFromGithub(String owner, String name) throws IOException, InterruptedException  {
        List<GithubPullRequestDTO> githubPullRequestDTOList = new ArrayList<>();
        this.setGraphQlGetPullRequestTotalCountQuery(owner, name);

        String responseJson = Objects.requireNonNull(this.webClientGraphQl.post()
                        .body(BodyInserters.fromObject(this.graphQlQuery))
                        .exchange()
                        .block())
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> paginationInfo = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("repository"))
                .map(repo -> repo.get("pullRequests"));

        if(paginationInfo.isPresent()) {
            double totalCount = paginationInfo.get().get("totalCount").asInt();

            List<GithubPullRequestLoaderThread> githubPullRequestLoaderThreadList = new ArrayList<>();

            if (totalCount != 0) {
                for (int i = 1; i <= Math.ceil(totalCount/100); i++) {
                    GithubPullRequestLoaderThread githubPullRequestLoaderThread =
                            new GithubPullRequestLoaderThread(
                                    githubPullRequestDTOList,
                                    owner,
                                    name,
                                    i);
                    githubPullRequestLoaderThreadList.add(githubPullRequestLoaderThread);
                    githubPullRequestLoaderThread.start();
                }

                for (GithubPullRequestLoaderThread thread: githubPullRequestLoaderThreadList) {
                    thread.join();
                }
            }
        } else {
            return null;
        }
        return githubPullRequestDTOList;
    }


    public JsonNode getAvatarURL(String owner) throws IOException {
        this.setGraphQlGetAvatarQuery(owner);
        String responseJson = Objects.requireNonNull(this.webClientGraphQl.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block())
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        Optional<JsonNode> avatar = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("search"))
                .map(search -> search.get("edges").get(0))
                .map(edges -> edges.get("node"))
                .map(node -> node.get("avatarUrl"));

        return avatar.orElse( null);
    }

    public boolean getCommentFromGithub(String owner, String name, Date lastUpdate) throws InterruptedException, IOException {
        try {
            String since = dateToISO8601(lastUpdate);
            String responseJson = Objects.requireNonNull(this.webClientRestAPI.get()
                            .uri("/" + owner + "/" + name + "/issues/comments?since=" + since)
                            .exchange()
                            .block())
                    .bodyToMono(String.class)
                    .block();
            ObjectMapper mapper = new ObjectMapper();

            Optional<JsonNode> commentList = Optional.ofNullable(mapper.readTree(responseJson));
            commentList.ifPresent(jsonNode -> jsonNode.forEach(entity -> {
                String tmp = entity.get("created_at").textValue();
                if(!tmp.equals(since)) {
                    GithubCommentDTO githubCommentDTO = new GithubCommentDTO();
                    githubCommentDTO.setRepoOwner(owner);
                    githubCommentDTO.setRepoName(name);
                    githubCommentDTO.setCreatedAt(entity.get("created_at"));
                    githubCommentDTO.setAuthor(entity.get("user").get("login").textValue());

                    githubCommentService.save(githubCommentDTO);
                }
            }));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

