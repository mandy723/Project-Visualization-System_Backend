package rvs.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rvs.demo.model.GitCommitBo;
import rvs.demo.service.GitCommitService;
import rvs.demo.service.GithubApiService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GithubApiController {

    private final GithubApiService githubApiService;
    private final GitCommitService gitCommitService;

    public GithubApiController(GithubApiService githubApiService, GitCommitService gitCommitService) {
        this.githubApiService = githubApiService;
        this.gitCommitService = gitCommitService;
    }

    //todo rename name -> repoName ..
    @PostMapping("/commits/{owner}/{name}")
    public ResponseEntity<String> postCommits(@PathVariable("owner") String owner, @PathVariable("name") String name) throws IOException {
        JsonNode responseJson = githubApiService.getCommits(owner, name);

        System.out.println("responseJson ========= ");
        System.out.println(responseJson);

        if(responseJson != null) {
            responseJson.forEach(entity->{
                DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
                Date committedDate =
                        isoParser.parseDateTime(entity.get("committedDate").toString().replace("\"", "")).toDate();
                //todo discuss
                if(gitCommitService.getCommit(owner, name, committedDate) == null) {
                    GitCommitBo gitCommitBo = new GitCommitBo(owner, name, committedDate);
                    gitCommitService.add(gitCommitBo);
                }
            });

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Saving commits to database complete");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Can't find repository");
    }

    @GetMapping("/commits/{owner}/{name}")
    public ResponseEntity<String> getCommits(@PathVariable("owner") String owner, @PathVariable("name") String name) throws IOException {
        List<GitCommitBo> commitBos = gitCommitService.getAllCommits(owner, name);
        System.out.println(commitBos.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .body(commitBos.toString());
    }
}
