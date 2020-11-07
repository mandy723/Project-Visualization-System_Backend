package rvs.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GithubApiService {

    private final WebClient webClient;

    private Map<String, Object> graphQl;

    private String token = "7a932d35a4ab732ccebd624311b0daa70fcd49af"; //todo get token from database

    public GithubApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com/graphql")
                .defaultHeader("Authorization", "Bearer " + token )
                .build();
    }

    private void setGraphQl(String owner, String name) {
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
        this.graphQl = graphQl;
    }

    public JsonNode getCommits(String owner, String name) throws IOException {
        this.setGraphQl(owner, name);
        //todo use thread get commits from Github
        String responseJson = this.webClient.post()
                .body(BodyInserters.fromObject(this.graphQl))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        System.out.println(responseJson);

        System.out.println("responseJson ====");
        System.out.println(responseJson);

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


}

//{
//repository(name: "react", owner: "facebook") {
//defaultBranchRef {
//target {
//... on Commit {
//history(first: 5) {
//nodes {
//committedDate
//additions
//deletions
//changedFiles
//author {
//email
//name
//}
//}
//}
//}
//}
//}
//issues(first: 5, orderBy: {field: CREATED_AT, direction: DESC}) {
//nodes {
//state
//closedAt
//createdAt
//title
//url
//}
//}
//primaryLanguage {
//name
//}
//}
//}
