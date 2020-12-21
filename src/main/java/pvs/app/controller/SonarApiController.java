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
import pvs.app.service.SonarApiService;

import java.io.IOException;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SonarApiController {
    static final Logger logger = LogManager.getLogger(SonarApiController.class.getName());
    private final SonarApiService sonarApiService;

    public SonarApiController(SonarApiService sonarApiService) {
        this.sonarApiService = sonarApiService;
    }

    @GetMapping("/sonar/{component}/coverage")
    public ResponseEntity<Double> getCoverage(@PathVariable("component") String component) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        logger.debug("coverage");
        Double coverage = sonarApiService.getSonarCodeCoverage(component);

        return ResponseEntity.status(HttpStatus.OK)
                .body(coverage);
    }
}
