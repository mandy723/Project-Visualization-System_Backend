package rvs.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GithubApiService {

    static final Logger logger = LogManager.getLogger(GithubApiService.class.getName());

    private final WebClient webClient;

    private Map<String, Object> graphQlQuery;

    private String token = System.getenv("PVS_GITHUB_TOKEN"); //todo get token from database

    public GithubApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com/graphql")
                .defaultHeader("Authorization", "Bearer " + token )
                .build();
    }

    private void setGraphQlGetCommitsQuery(String owner, String name) {
        //todo get data since last commit date to now
        Map<String, Object> graphQl = new HashMap<>();
        graphQl.put("query", "{repository(owner: \"" + owner + "\", name:\"" + name + "\") {" +
                                "defaultBranchRef {" +
                                    "target {" +
                                        "... on Commit {" +
                                            "history (first:100) {" +
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

    public JsonNode getCommits(String owner, String name) throws IOException {
        this.setGraphQlGetCommitsQuery(owner, name);
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
        Optional<JsonNode> commits = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                    .map(data -> data.get("repository"))
                        .map(repo -> repo.get("defaultBranchRef"))
                            .map(branch -> branch.get("target"))
                                .map(tag -> tag.get("history"))
                                    .map(hist -> hist.get("nodes"));
        return commits.orElse( null);
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
}