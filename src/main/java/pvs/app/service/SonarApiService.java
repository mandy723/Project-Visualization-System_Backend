package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;

@Service
public class SonarApiService {

    static final Logger logger = LogManager.getLogger(SonarApiService.class.getName());

    private final WebClient webClient;

    private String token = System.getenv("PVS_SONAR_TOKEN"); //todo get token from database

    public SonarApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://140.124.181.143:9002/api")
                .defaultHeader("Authorization", "Bearer " + token )
                .build();
    }

    public double getSonarCodeCoverage(String component) throws IOException {
        String responseJson = this.webClient.get()
                .uri("/measures/component?component=" +component + "&metricKeys=coverage")
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> coverageJson = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("component"))
                .map(comp -> comp.get("measures"));

        double coverage = coverageJson.get().get(0).get("value").asDouble();
        return coverage;
    }
}