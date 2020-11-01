package rvs.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GithubApiService {

    private final WebClient webClient;

    private Map<String, Object> graphQl;

    private String token = "adaeef7758abc5142759c11d037147ee595642f3"; //todo get token from database

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


    public GithubApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com/graphql").build();
    }

    public JsonNode getCommits(String owner, String name) throws IOException {
        this.setGraphQl(owner, name);

        String responseJson = webClient.post()
                .body(BodyInserters.fromObject(this.graphQl))
                .header("Authorization", "Bearer " + token )
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseObj = mapper.readTree(responseJson);

        if(!responseObj.has("errors")) {
            return mapper.readTree(responseJson)
                    .get("data")
                        .get("repository")
                            .get("defaultBranchRef")
                                .get("target")
                                    .get("history")
                                        .get("nodes");
        }
        return null;
    }


}
