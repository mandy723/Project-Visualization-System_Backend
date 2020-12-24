package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.dto.GithubCommitDTO;
import pvs.app.dto.GithubIssueDTO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@SuppressWarnings("squid:S1192")
public class GithubApiService {

    static final Logger logger = LogManager.getLogger(GithubApiService.class.getName());

    private final WebClient webClient;

    private Map<String, Object> graphQlQuery;

    private String token = System.getenv("PVS_GITHUB_TOKEN");

    private final GithubCommitService githubCommitService;

    public GithubApiService(WebClient.Builder webClientBuilder, GithubCommitService githubCommitService) {
        this.githubCommitService = githubCommitService;
        this.webClient = webClientBuilder.baseUrl("https://api.github.com/graphql")
                .defaultHeader("Authorization", "Bearer " + token )
                .build();
    }

    private String dateToISO8601(Date date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
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
                                "issues (first: 100) {" +
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

    public void getCommitsFromGithub(String owner, String name, Date lastUpdate) throws IOException, InterruptedException {
        this.setGraphQlGetCommitsTotalCountAndCursorQuery(owner, name, lastUpdate);

        String responseJson = this.webClient.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> paginationInfo = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("repository"))
                .map(repo -> repo.get("defaultBranchRef"))
                .map(branch -> branch.get("target"))
                .map(tag -> tag.get("history"));

        double totalCount = paginationInfo.get().get("totalCount").asInt();
        List<GithubCommitLoaderThread> githubCommitLoaderThreadList = new ArrayList<>();

        if (totalCount != 0) {
            String cursor = paginationInfo.get().get("pageInfo").get("startCursor").textValue()
                    .split(" ")[0];
            for (int i = 1; i <= Math.ceil(totalCount/100); i++) {
                GithubCommitLoaderThread githubCommitLoaderThread =
                        new GithubCommitLoaderThread(
                                this.webClient,
                                this.githubCommitService,
                                owner,
                                name,
                                cursor + " " + (i*100));
                githubCommitLoaderThreadList.add(githubCommitLoaderThread);
                githubCommitLoaderThread.start();
            }

            for (GithubCommitLoaderThread thread: githubCommitLoaderThreadList) {
                thread.join();
            }
        }
    }

    public List<GithubIssueDTO> getIssuesFromGithub(String owner, String name) throws IOException, InterruptedException  {
        List<GithubIssueDTO> githubIssueDTOList = new ArrayList<>();
        this.setGraphQlGetIssuesTotalCountQuery(owner, name);

        String responseJson = this.webClient.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> paginationInfo = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("repository"))
                .map(repo -> repo.get("issues"));

        logger.debug(paginationInfo);
        double totalCount = paginationInfo.get().get("totalCount").asInt();
        List<GithubIssueLoaderThread> githubIssueLoaderThreadList = new ArrayList<>();

        if (totalCount != 0) {

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
        return githubIssueDTOList;
    }

    public JsonNode getAvatarURL(String owner) throws IOException {
        this.setGraphQlGetAvatarQuery(owner);
        String responseJson = this.webClient.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        logger.debug("responseJson ====");
        logger.debug(responseJson);

        ObjectMapper mapper = new ObjectMapper();
        Optional<JsonNode> avatar = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("search"))
                .map(search -> search.get("edges").get(0))
                .map(edges -> edges.get("node"))
                .map(node -> node.get("avatarUrl"));
        return avatar.orElse( null);
    }
}

class GithubCommitLoaderThread extends Thread {

    private static Object lock = new Object();

    static final Logger logger = LogManager.getLogger(GithubCommitLoaderThread.class.getName());


    private GithubCommitService githubCommitService;
    private String repoOwner;
    private String repoName;
    private String cursor;
    private WebClient webClient;

    GithubCommitLoaderThread(WebClient webClient, GithubCommitService githubCommitService, String repoOwner, String repoName, String cursor) {
        this.githubCommitService = githubCommitService;
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.cursor = cursor;
        this.webClient = webClient;
    }

    @Override
    public void run() {
        Map<String, Object> graphQlQuery = new HashMap<>();
        graphQlQuery.put("query", "{repository(owner: \"" + this.repoOwner + "\", name:\"" + this.repoName + "\") {" +
                "defaultBranchRef {" +
                "target {" +
                "... on Commit {" +
                "history (last:100, before: \"" + this.cursor + "\") {" +
                "nodes {" +
                "committedDate\n" +
                "additions\n" +
                "deletions\n" +
                "changedFiles\n" +
                "author {" +
                "email\n" +
                "name\n" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}}");

        String responseJson = this.webClient.post()
                .body(BodyInserters.fromObject(graphQlQuery))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> commits = null;
        try {
            commits = Optional.ofNullable(mapper.readTree(responseJson))
                    .map(resp -> resp.get("data"))
                    .map(data -> data.get("repository"))
                    .map(repo -> repo.get("defaultBranchRef"))
                    .map(branch -> branch.get("target"))
                    .map(tag -> tag.get("history"))
                    .map(hist -> hist.get("nodes"));
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }

        commits.get().forEach(entity->{
            GithubCommitDTO githubCommitDTO = new GithubCommitDTO();
            githubCommitDTO.setRepoOwner(repoOwner);
            githubCommitDTO.setRepoName(repoName);
            githubCommitDTO.setAdditions(Integer.parseInt(entity.get("additions").toString()));
            githubCommitDTO.setDeletions(Integer.parseInt(entity.get("deletions").toString()));
            githubCommitDTO.setCommittedDate(entity.get("committedDate"));
            githubCommitDTO.setAuthor(Optional.ofNullable(entity.get("author")));

            synchronized (lock) {
                githubCommitService.save(githubCommitDTO);
            }
        });
    }
}

class GithubIssueLoaderThread extends Thread {

    private final String token = System.getenv("PVS_GITHUB_TOKEN");
    private static Object lock = new Object();
    static final Logger logger = LogManager.getLogger(GithubIssueLoaderThread.class.getName());

    private List<GithubIssueDTO> githubIssueDTOList;
    private String repoOwner;
    private String repoName;
    private WebClient webClient;
    private int page;


    GithubIssueLoaderThread(List<GithubIssueDTO> githubIssueDTOList, String repoOwner, String repoName, int page) {
        this.webClient = WebClient.builder().baseUrl("https://api.github.com/repos")
                .defaultHeader("Authorization", "Bearer " + token )
                .build();
        this.githubIssueDTOList = githubIssueDTOList;
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.page = page;
    }

    @Override
    public void run() {
        String responseJson = this.webClient.get()
                .uri("/"+ this.repoOwner +"/"+ this.repoName +"/issues?page="+ this.page +"&per_page=100&state=all")
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> issueList = null;
        try {
            issueList = Optional.ofNullable(mapper.readTree(responseJson));
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }


        issueList.get().forEach(entity->{
            GithubIssueDTO githubIssueDTO = new GithubIssueDTO();
            githubIssueDTO.setRepoOwner(repoOwner);
            githubIssueDTO.setRepoName(repoName);
            githubIssueDTO.setCreatedAt(entity.get("created_at"));
            githubIssueDTO.setClosedAt(entity.get("closed_at"));

            synchronized (lock) {
                githubIssueDTOList.add(githubIssueDTO);
            }
        });
    }
}