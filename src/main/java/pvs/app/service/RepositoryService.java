package pvs.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@SuppressWarnings("squid:S1192")
public class RepositoryService {
    private final WebClient webClient;

    private String token = System.getenv("PVS_GITHUB_TOKEN");

    static final Logger logger = LogManager.getLogger(RepositoryService.class.getName());

    public RepositoryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader("Authorization", "Bearer " + token )
                .build();
    }

    public boolean checkGithubURL(String url) {
        if(!url.contains("github.com")){
            return false;
        }
        String targetURL = url.replace("github.com", "api.github.com/repos");
        logger.debug(targetURL);

        AtomicBoolean result = new AtomicBoolean(false);
        this.webClient
            .get()
            .uri(targetURL)
            .exchange()
            .doAfterSuccessOrError((clientResponse, throwable) ->
                result.set(clientResponse.statusCode().equals(HttpStatus.OK))
            )
            .block();
        logger.debug(result.get());
        return result.get();
    }

    public boolean checkSonarURL(String url) {
        if(!url.contains("http://140.124.181.143/")){
            return false;
        }
        String targetURL = url.replace("dashboard?id", "api/components/show?component");
        AtomicBoolean result = new AtomicBoolean(false);

        this.webClient
            .get()
            .uri(targetURL)
            .exchange()
            .doAfterSuccessOrError((clientResponse, throwable) ->
                    result.set(clientResponse.statusCode().equals(HttpStatus.OK))
            )
            .block();
        return result.get();
    }
}
