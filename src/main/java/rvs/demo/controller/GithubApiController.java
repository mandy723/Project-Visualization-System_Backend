package rvs.demo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GithubApiController {
    String baseUrl = "https://api.github.com/graphql";
    String token = "adaeef7758abc5142759c11d037147ee595642f3";


    @PostMapping("/commits/{owner}/{name}")
    @ResponseBody
    public void postCommits(@PathVariable("owner") String owner, @PathVariable("name") String name) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("query", "{repository(owner: \"facebook\", name:\"react\") {" +
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

        WebClient webClient = WebClient.create(baseUrl);
        String responseJson = webClient.post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(params))
        .header("Authorization", "Bearer adaeef7758abc5142759c11d037147ee595642f3" )
        .exchange()
        .block()
        .bodyToMono(String.class)
        .block();;
        System.out.println("responseJson ========= ");
        System.out.println(responseJson);


    }

    @GetMapping("/commits/{owner}/{name}")
    public String getCommits(@PathVariable("owner") String owner, @PathVariable("name") String name) throws IOException {

        return "123";
    }
}
