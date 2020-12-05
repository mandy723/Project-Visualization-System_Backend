package pvs.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RepositoryService {
    private final WebClient webClient;

    private String token = System.getenv("PVS_GITHUB_TOKEN"); //todo get token from database

    static final Logger logger = LogManager.getLogger(RepositoryService.class.getName());

    public RepositoryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader("Authorization", "Bearer " + token )
                .build();
    }

    public boolean checkURL(String URL) {
        String targetURL = URL.replace("github.com", "api.github.com/repos");
        AtomicBoolean result = new AtomicBoolean(false);
        this.webClient
            .get()
            .uri(targetURL)
            .exchange()
            .doAfterSuccessOrError((clientResponse, throwable) -> {
                result.set(clientResponse.statusCode().equals(HttpStatus.OK));
            })
            .block();

        return result.get();
    }
}
