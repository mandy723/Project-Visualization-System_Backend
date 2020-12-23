package pvs.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pvs.app.dto.BugDTO;
import pvs.app.dto.CodeCoverageDTO;
import pvs.app.dto.CodeSmellDTO;
import pvs.app.dto.DuplicationDTO;
import pvs.app.service.SonarApiService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SonarApiController {
    static final Logger logger = LogManager.getLogger(SonarApiController.class.getName());
    private final SonarApiService sonarApiService;

    public SonarApiController(SonarApiService sonarApiService) {
        this.sonarApiService = sonarApiService;
    }

    @GetMapping("/sonar/{component}/coverage")
    public ResponseEntity<String> getCoverage(@PathVariable("component") String component) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.debug("coverage");
        List<CodeCoverageDTO> coverages = sonarApiService.getSonarCodeCoverage(component);

        String coverageString = objectMapper.writeValueAsString(coverages);

        return ResponseEntity.status(HttpStatus.OK)
                .body(coverageString);
    }

    @GetMapping("/sonar/{component}/bug")
    public ResponseEntity<String> getBug(@PathVariable("component") String component) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.debug("bug");
        List<BugDTO> bugList = sonarApiService.getSonarBug(component);

        String bugListString = objectMapper.writeValueAsString(bugList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(bugListString);
    }

    @GetMapping("/sonar/{component}/code_smell")
    public ResponseEntity<String> getCodeSmell(@PathVariable("component") String component) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.debug("code smell");
        List<CodeSmellDTO> codeSmellList = sonarApiService.getSonarCodeSmell(component);

        String codeSmellListString = objectMapper.writeValueAsString(codeSmellList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(codeSmellListString);
    }

    @GetMapping("/sonar/{component}/duplication")
    public ResponseEntity<String> getDuplication(@PathVariable("component") String component) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.debug("duplication");
        List<DuplicationDTO> duplicationList = sonarApiService.getDuplication(component);

        String duplicationListString = objectMapper.writeValueAsString(duplicationList);

        return ResponseEntity.status(HttpStatus.OK)
                .body(duplicationListString);
    }
}
