package pvs.app.service.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.dto.GithubPullRequestDTO;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GithubPullRequestLoaderThread extends Thread {
    private static final Object lock = new Object();
    private final WebClient webClient;
    private final List<GithubPullRequestDTO> githubPullRequestDTOList;
    private final String repoOwner;
    private final String repoName;
    private final int page;

    public GithubPullRequestLoaderThread(WebClient webClient, List<GithubPullRequestDTO> githubPullRequestDTOList, String repoOwner, String repoName, int page) {
        String token = System.getenv("PVS_GITHUB_TOKEN");
        this.webClient = webClient;
        this.githubPullRequestDTOList = githubPullRequestDTOList;
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.page = page;
    }

    @Override
    public void run() {
        String responseJson = Objects.requireNonNull(this.webClient.get()
                        .uri("/" + this.repoOwner + "/" + this.repoName + "/pulls?page=" + this.page + "&per_page=100&state=all")
                        .exchange()
                        .block())
                .bodyToMono(String.class)
                .block();
        ObjectMapper mapper = new ObjectMapper();

        try {
            Optional<JsonNode> pullRequestList = Optional.ofNullable(mapper.readTree(responseJson));
            pullRequestList.ifPresent(jsonNode -> jsonNode.forEach(entity -> {
                GithubPullRequestDTO githubPullRequestDTO = new GithubPullRequestDTO();
                githubPullRequestDTO.setState(entity.get("state").textValue());
                githubPullRequestDTO.setCreatedAt(entity.get("created_at"));
                githubPullRequestDTO.setMergedAt(entity.get("merged_at"));
                githubPullRequestDTO.setAuthor(entity.get("user").get("login").textValue());

                synchronized (lock) {
                    githubPullRequestDTOList.add(githubPullRequestDTO);
                }
            }));
        } catch (IOException e) {
            Thread.currentThread().interrupt();
        }
    }
}
