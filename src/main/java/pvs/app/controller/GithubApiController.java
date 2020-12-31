package pvs.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pvs.app.dto.GithubCommitDTO;
import pvs.app.dto.GithubIssueDTO;
import pvs.app.service.GithubCommitService;
import pvs.app.service.GithubApiService;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GithubApiController {

    static final Logger logger = LogManager.getLogger(GithubApiController.class.getName());

    private final GithubApiService githubApiService;
    private final GithubCommitService githubCommitService;

    public GithubApiController(GithubApiService githubApiService, GithubCommitService githubCommitService) {
        this.githubApiService = githubApiService;
        this.githubCommitService = githubCommitService;
    }

    @SneakyThrows
    @PostMapping("/commits/{repoOwner}/{repoName}")
    public ResponseEntity<String> postCommits(@PathVariable("repoOwner") String repoOwner, @PathVariable("repoName") String repoName) throws IOException {
        Date lastUpdate;
        GithubCommitDTO githubCommitDTO = githubCommitService.getLastCommit(repoOwner, repoName);
        if (null == githubCommitDTO) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1970, Calendar.JANUARY, 1);
            lastUpdate = calendar.getTime();
        } else {
            lastUpdate = githubCommitDTO.getCommittedDate();
        }

        githubApiService.getCommitsFromGithub(repoOwner, repoName, lastUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("/commits/{repoOwner}/{repoName}")
    public ResponseEntity<String> getCommits(@PathVariable("repoOwner") String repoOwner, @PathVariable("repoName") String repoName) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        List<GithubCommitDTO> githubCommitDTOs = githubCommitService.getAllCommits(repoOwner, repoName);

        String githubCommitDTOsJson = objectMapper.writeValueAsString(githubCommitDTOs);

        return ResponseEntity.status(HttpStatus.OK)
                .body(githubCommitDTOsJson);
    }

    @GetMapping("/issues/{repoOwner}/{repoName}")
    public ResponseEntity<String> getIssues(@PathVariable("repoOwner") String repoOwner, @PathVariable("repoName") String repoName) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<GithubIssueDTO> githubIssueDTOs = githubApiService.getIssuesFromGithub(repoOwner, repoName);
        String githubIssueDTOsJson = objectMapper.writeValueAsString(githubIssueDTOs);

        return ResponseEntity.status(HttpStatus.OK)
                .body(githubIssueDTOsJson);
    }
}
