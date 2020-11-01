package rvs.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rvs.demo.model.GitCommitBo;
import rvs.demo.service.GitCommitService;
import rvs.demo.service.GithubApiService;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GithubApiController {

    @Autowired
    private GithubApiService githubApiService;

    @Autowired
    private GitCommitService gitCommitService;

    @PostMapping("/commits/{owner}/{name}")
    public ResponseEntity<String> postCommits(@PathVariable("owner") String owner, @PathVariable("name") String name) throws IOException {
        JsonNode responseJson = githubApiService.getCommits(owner, name);

        System.out.println("responseJson ========= ");
        System.out.println(responseJson);

        if(responseJson != null) {
            //todo use thread save commits to database
            responseJson.forEach(entity->{
                DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis();
                Date committedDate =
                        isoParser.parseDateTime(entity.get("committedDate").toString().replace("\"", "")).toDate();

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
        List<GitCommitBo> commitBos = gitCommitService.getAll(owner, name);
        System.out.println(commitBos.toString());

        return ResponseEntity.status(HttpStatus.OK)
                .body(commitBos.toString());
    }
}
