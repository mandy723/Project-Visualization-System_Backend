package pvs.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.dto.CodeCoverageDTO;

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

    public List<CodeCoverageDTO> getSonarCodeCoverage(String component) throws IOException {
        String responseJson = this.webClient.get()
                .uri("/measures/search_history?component=" +component + "&metrics=coverage")
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();

        Optional<JsonNode> coverageJsonNodes = Optional.ofNullable(mapper.readTree(responseJson))
                .map(resp -> resp.get("measures"));

        JsonNode coverageArrayNode = coverageJsonNodes.get().get(0).get("history");

        List<CodeCoverageDTO> coverages = new ArrayList<>();

        if(coverageArrayNode.isArray()) {
            for(final JsonNode jsonNode : coverageArrayNode) {
                DateTimeFormatter isoParser = ISODateTimeFormat.dateTimeNoMillis().withLocale(Locale.TAIWAN);

                logger.debug(jsonNode);

                Date date =
                        isoParser.parseDateTime(jsonNode.get("date").textValue().replace("\"", ""))
                                 .toDate();
                coverages.add(new CodeCoverageDTO(date, jsonNode.get("value").asDouble()));
            }
        }

        return coverages;
    }
}