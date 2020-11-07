package rvs.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rvs.demo.model.GithubCommitDTO;
import rvs.demo.service.GithubCommitService;
import rvs.demo.service.GithubApiService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GithubApiController {

    private final GithubApiService githubApiService;
    private final GithubCommitService githubCommitService;

    public GithubApiController(GithubApiService githubApiService, GithubCommitService githubCommitService) {
        this.githubApiService = githubApiService;
        this.githubCommitService = githubCommitService;
    }

    @PostMapping("/commits/{repoOwner}/{repoName}")
    public ResponseEntity<String> postCommits(@PathVariable("repoOwner") String repoOwner, @PathVariable("repoName") String repoName) throws IOException {
        JsonNode responseJson = githubApiService.getCommits(repoOwner, repoName);

        System.out.println("responseJson ========= ");
        System.out.println(responseJson);

        if(responseJson != null) {
            responseJson.forEach(entity->{
                //todo discuss
                    GithubCommitDTO githubCommitDTO = new GithubCommitDTO();
                    githubCommitDTO.setRepoOwner(repoOwner);
                    githubCommitDTO.setRepoName(repoName);
                    githubCommitDTO.setAdditions(Integer.parseInt(entity.get("additions").toString()));
                    githubCommitDTO.setDeletions(Integer.parseInt(entity.get("deletions").toString()));
                    githubCommitDTO.setCommittedDate(entity.get("committedDate"));
                    githubCommitDTO.setAuthor(Optional.ofNullable(entity.get("author")));
                    githubCommitDTO.setChangeFiles(Integer.parseInt(entity.get("changedFiles").toString()));
                    githubCommitService.add(githubCommitDTO);
                });
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Saving commits to database complete");

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Can't find repository");
    }

    @GetMapping("/commits/{repoOwner}/{repoName}")
    public ResponseEntity<String> getCommits(@PathVariable("repoOwner") String repoOwner, @PathVariable("repoName") String repoName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<GithubCommitDTO> githubCommitDTOS = githubCommitService.getAllCommits(repoOwner, repoName);

        String githubCommitDTOSJson = objectMapper.writeValueAsString(githubCommitDTOS);
        System.out.println(githubCommitDTOSJson);

        return ResponseEntity.status(HttpStatus.OK)
                .body(githubCommitDTOSJson);
    }
}
