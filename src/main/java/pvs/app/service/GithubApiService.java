package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.dto.GithubCommitDTO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GithubApiService {

    static final Logger logger = LogManager.getLogger(GithubApiService.class.getName());

    private final WebClient webClient;

    private Map<String, Object> graphQlQuery;

    private String token = System.getenv("PVS_GITHUB_TOKEN"); //todo get token from database

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
        //todo get data since last commit date to now
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

    private void setGraphQlGetIssuesQuery(String owner, String name) {
        //todo get data since last commit date to now
        Map<String, Object> graphQl = new HashMap<>();
        graphQl.put("query", "{repository(owner: \"" + owner + "\", name:\"" + name + "\") {" +
                                "issues (first:100) {" +
                                    "nodes {" +
                                        "createdAt\n" +
                                        "closedAt\n" +
                                    "}" +
                                "}" +
                            "}}");
        this.graphQlQuery = graphQl;
    }

    private void setGraphQlGetAvatarQuery(String owner) {
        //todo get data since last commit date to now
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
        logger.debug(lastUpdate);
        this.setGraphQlGetCommitsTotalCountAndCursorQuery(owner, name, lastUpdate);

        String responseJson = this.webClient.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        logger.debug("responseJson ====");
        logger.debug(responseJson);

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
                                cursor + " " + String.valueOf(i*100));
                githubCommitLoaderThreadList.add(githubCommitLoaderThread);
                githubCommitLoaderThread.start();
            }

            for (GithubCommitLoaderThread thread: githubCommitLoaderThreadList) {
                thread.join();
            }
        }
//
//        this.setGraphQlGetCommitsQuery(owner, name, lastUpdate);
//        // todo use thread get commits from Github
//        responseJson = this.webClient.post()
//                .body(BodyInserters.fromObject(this.graphQlQuery))
//                .exchange()
//                .block()
//                .bodyToMono(String.class)
//                .block();
//
//        logger.debug("responseJson ====");
//        logger.debug(responseJson);
//
//        Optional<JsonNode> commits = Optional.ofNullable(mapper.readTree(responseJson))
//                .map(resp -> resp.get("data"))
//                    .map(data -> data.get("repository"))
//                        .map(repo -> repo.get("defaultBranchRef"))
//                            .map(branch -> branch.get("target"))
//                                .map(tag -> tag.get("history"))
//                                    .map(hist -> hist.get("nodes"));
//
//        commits.get().forEach(entity->{
//            //todo discuss
//            GithubCommitDTO githubCommitDTO = new GithubCommitDTO();
//            githubCommitDTO.setRepoOwner(owner);
//            githubCommitDTO.setRepoName(name);
//            githubCommitDTO.setAdditions(Integer.parseInt(entity.get("additions").toString()));
//            githubCommitDTO.setDeletions(Integer.parseInt(entity.get("deletions").toString()));
//            githubCommitDTO.setCommittedDate(entity.get("committedDate"));
//            githubCommitDTO.setAuthor(Optional.ofNullable(entity.get("author")));
//            githubCommitDTO.setChangeFiles(Integer.parseInt(entity.get("changedFiles").toString()));
//            githubCommitService.save(githubCommitDTO);
//        });
//
//        return commits.orElse( null);
    }

    public JsonNode getIssues(String owner, String name) throws IOException {
        this.setGraphQlGetIssuesQuery(owner, name);
        //todo use thread get commits from Github
        String responseJson = this.webClient.post()
                .body(BodyInserters.fromObject(this.graphQlQuery))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        logger.debug("responseJson ====");
        logger.debug(responseJson);

        ObjectMapper mapper = new ObjectMapper();
        Optional<JsonNode> issues = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                    .map(data -> data.get("repository"))
                        .map(repo -> repo.get("issues"))
                            .map(issue -> issue.get("nodes"));
        return issues.orElse( null);
    }

    public JsonNode getAvatarURL(String owner) throws IOException {
        this.setGraphQlGetAvatarQuery(owner);
        //todo use thread get commits from Github
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
    private static int i = 0;

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

    @SneakyThrows
    public void run() {
        logger.debug(i++);
        //todo get data since last commit date to now
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

        Optional<JsonNode> commits = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                .map(data -> data.get("repository"))
                .map(repo -> repo.get("defaultBranchRef"))
                .map(branch -> branch.get("target"))
                .map(tag -> tag.get("history"))
                .map(hist -> hist.get("nodes"));

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