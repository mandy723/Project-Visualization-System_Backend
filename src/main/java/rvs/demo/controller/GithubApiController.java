package rvs.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rvs.demo.service.GithubApiService;

import java.io.IOException;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GithubApiController {

    @Autowired
    private GithubApiService githubApiService;

    @PostMapping("/commits/{owner}/{name}")
    public ResponseEntity<String> postCommits(@PathVariable("owner") String owner, @PathVariable("name") String name) throws IOException {
        JsonNode responseJson = githubApiService.getCommits(owner, name);

        System.out.println("responseJson ========= ");
        System.out.println(responseJson);

        if(responseJson != null) {
            //todo save commits to database
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("getCommits end");
    }

    @GetMapping("/commits/{owner}/{name}")
    public String getCommits(@PathVariable("owner") String owner, @PathVariable("name") String name) throws IOException {
        //todo get commits from database
        return "123";
    }
}
