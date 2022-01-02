package pvs.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@SuppressWarnings("squid:S1192")
public class RepositoryService {
    private final WebClient githubWebClient;
    private final WebClient sonarWebClient;

    private final String githubToken = System.getenv("PVS_GITHUB_TOKEN");
    private final String sonarToken = System.getenv("PVS_SONAR_TOKEN");

    public RepositoryService( @Value("${webClient.baseUrl.test}") String baseUrl) {
        this.sonarWebClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeaders(header -> header.setBasicAuth(sonarToken, ""))
                .build();
        this.githubWebClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + githubToken )
                .build();
    }

    public boolean checkGithubURL(String url) {
        if(!url.contains("github.com")){
            return false;
        }
        String targetURL = url.replace("github.com", "api.github.com/repos");
        AtomicBoolean result = new AtomicBoolean(false);

        this.githubWebClient
                .get()
                .uri(targetURL)
                .exchange()
                .doOnSuccess(clientResponse ->
                    result.set(clientResponse.statusCode().equals(HttpStatus.OK))
                )
                .block();
        return result.get();
    }

    public boolean checkSonarURL(String url) {
        if(!url.contains("localhost")){
            return false;
        }
//        http://localhost:9000/api/components/show?component=SEWinWinWin_PVS_backend
//        http://localhost:9000/dashboard?id=SEWinWinWin_PVS_backend
        String targetURL = url.replace("dashboard?id", "api/components/show?component");
        AtomicBoolean result = new AtomicBoolean(false);

        this.sonarWebClient
                .get()
                .uri(targetURL)
                .exchange()
                .doOnSuccess(clientResponse ->
                        result.set(clientResponse.statusCode().equals(HttpStatus.OK))
                )
                .block();
        return result.get();
    }
}
