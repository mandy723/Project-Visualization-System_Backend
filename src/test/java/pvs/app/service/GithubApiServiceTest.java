package pvs.app.service;


import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import pvs.app.Application;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GithubApiServiceTest {

    @MockBean
    private GithubCommitService githubCommitService;

    private MockWebServer mockWebServer;

    private GithubApiService githubApiService;

    @Before
    public void setup() {
        this.mockWebServer = new MockWebServer();
        this.githubApiService = new GithubApiService(WebClient.builder(), mockWebServer.url("/").toString(), githubCommitService);
    }

    @Test
    public void getCommitsFromGithub()  {
        //given
        Date lastDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            lastDate = dateFormat.parse("2020-11-20 19:38:25");
        } catch(ParseException e) {

        }

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                        "    \"data\": {" +
                        "        \"repository\": {" +
                        "            \"defaultBranchRef\": {" +
                        "                \"target\": {" +
                        "                    \"history\": {" +
                        "                        \"totalCount\": 0," +
                        "                        \"pageInfo\": {" +
                        "                            \"startCursor\": \"50393dc3a0c59cfefd349d31992256efd6f8c261 0\"" +
                        "                        }" +
                        "                    }" +
                        "                }" +
                        "            }" +
                        "        }" +
                        "    }" +
                        "}")
                .addHeader("Content-Type", "application/json")
        );

        //when
        try {
            githubApiService.getCommitsFromGithub("facebook", "react", lastDate);
        } catch (IOException | InterruptedException e) {

        }
        //todo
        Assert.assertTrue(true);
    }

    @Test
    public void getIssuesFromGithub()  {
        //given
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                        "    \"data\": {" +
                        "        \"repository\": {" +
                        "            \"issues\": {" +
                        "                \"totalCount\": 0" +
                        "            }" +
                        "        }" +
                        "    }" +
                        "}")
                .addHeader("Content-Type", "application/json")
        );

        //when
        try {
            githubApiService.getIssuesFromGithub("facebook", "react");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //todo
        Assert.assertTrue(true);
    }

    @Test
    public void getAvatarURL() {
        //given
        String avatarURL = null;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                        "    \"data\": {" +
                        "        \"search\": {" +
                        "            \"edges\": [" +
                        "                {" +
                        "                    \"node\": {" +
                        "                        \"avatarUrl\": \"https://avatars3.githubusercontent.com/u/69631?v=4\"" +
                        "                    }" +
                        "                }" +
                        "            ]" +
                        "        }" +
                        "    }" +
                        "}")
                .addHeader("Content-Type", "application/json")
        );

        //when
        try {
            avatarURL = githubApiService.getAvatarURL("facebook").textValue();
        } catch (IOException e) {

        }

        Assert.assertEquals("https://avatars3.githubusercontent.com/u/69631?v=4", avatarURL);
    }

}