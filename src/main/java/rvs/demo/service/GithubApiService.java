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

    private String token = "062fe366aed320c7b0b1bb5fbedb48bc14ffc821"; //todo get token from database

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
                                                    "committedDate" +
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
        JsonNode responseObj = mapper.readTree(responseJson);

        Optional<JsonNode> committedDates = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("data"))
                    .map(data -> data.get("repository"))
                        .map(repo -> repo.get("defaultBranchRef"))
                            .map(branch -> branch.get("target"))
                                .map(tag -> tag.get("history"))
                                    .map(hist -> hist.get("nodes"));

        return committedDates.orElse(null);
    }


}
