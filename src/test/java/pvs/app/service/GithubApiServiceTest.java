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
import pvs.app.dto.GithubIssueDTO;
import pvs.app.dto.GithubPullRequestDTO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GithubApiServiceTest {

    @MockBean
    private GithubCommitService githubCommitService;

    @MockBean
    private GithubCommentService githubCommentService;

    private MockWebServer mockWebServer;

    private GithubApiService githubApiService;

    @Before
    public void setup() {
        this.mockWebServer = new MockWebServer();
        this.githubApiService = new GithubApiService(WebClient.builder(), mockWebServer.url("/").toString(), mockWebServer.url("/").toString(), githubCommitService, githubCommentService);
    }

    @Test
    public void getCommitsFromGithub_notRunThread() throws ParseException {
        //given
        boolean result = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lastDate = dateFormat.parse("2020-11-20 19:38:25");

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
            result = githubApiService.getCommitsFromGithub("facebook", "react", lastDate);
        } catch (IOException | InterruptedException e) {

        }
        Assert.assertTrue(result);
    }

    @Test
    public void getCommitsFromGithub_runThread() throws ParseException {
        //given
        boolean result = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lastDate = dateFormat.parse("2020-11-20 19:38:25");

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                        "    \"data\": {" +
                        "        \"repository\": {" +
                        "            \"defaultBranchRef\": {" +
                        "                \"target\": {" +
                        "                    \"history\": {" +
                        "                        \"totalCount\": 1," +
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

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\n" +
                        "    \"data\": {\n" +
                        "        \"repository\": {\n" +
                        "            \"defaultBranchRef\": {\n" +
                        "                \"target\": {\n" +
                        "                    \"history\": {\n" +
                        "                        \"nodes\": [\n" +
                        "                            {\n" +
                        "                                \"committedDate\": \"2014-10-31T19:39:38Z\",\n" +
                        "                                \"additions\": 0,\n" +
                        "                                \"deletions\": 1,\n" +
                        "                                \"changedFiles\": 1,\n" +
                        "                                \"author\": {\n" +
                        "                                    \"email\": \"sebastian@calyptus.eu\",\n" +
                        "                                    \"name\": \"Sebastian Markbåge\"\n" +
                        "                                }\n" +
                        "                            }\n" +
                        "                        ]\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}").addHeader("Content-Type", "application/json"));

        //when
        try {
            result = githubApiService.getCommitsFromGithub("facebook", "react", lastDate);
        } catch (IOException | InterruptedException e) {

        }
        Assert.assertTrue(result);
    }

    @Test
    public void getIssuesFromGithub_notRunThread()  {
        //given
        List<GithubIssueDTO> result = new ArrayList<>();
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
            result = githubApiService.getIssuesFromGithub("facebook", "react");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(0, result.size());
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

    @Test
    public void getPullRequestFromGithub_notRunThread(){
        //given
        List<GithubPullRequestDTO> result = new ArrayList<>();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                            "\"data\": {" +
                                "\"repository\": {" +
                                    "\"pullRequests\": {" +
                                        "\"totalCount\": 0" +
                                    "}" +
                                "}" +
                            "}" +
                        "}")
                .addHeader("Content-Type", "application/json")
        );

        //when
        try {
            result = githubApiService.getPullRequestFromGithub("facebook", "react");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void getPullRequestFromGithub_runThread() throws ParseException {
        //given
        List<GithubPullRequestDTO> result = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lastDate = dateFormat.parse("2020-11-20 19:38:25");

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{" +
                            "\"data\": {" +
                                "\"repository\": {" +
                                    "\"pullRequests\": {" +
                                        "\"totalCount\": 1" +
                                    "}" +
                                "}" +
                            "}" +
                        "}")
                .addHeader("Content-Type", "application/json")
        );

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[{" +
                            "\"state\": \"open\"," +
                            "\"user\": {" +
                                "\"login\": \"jonatan-ivanov\"" +
                            "}," +
                            "\"created_at\": \"2021-12-05T01:52:10Z\"," +
                            "\"merged_at\": \"2021-12-10T01:52:10Z\"" +
                        "}]")
                .addHeader("Content-Type", "application/json")
        );
//        mockWebServer.enqueue(new MockResponse().setBody());

        //when
        try {
            result = githubApiService.getPullRequestFromGithub("facebook", "react");
        } catch (IOException | InterruptedException e) {

        }
        Assert.assertEquals(1, result.size());
    }

//    @Test
//    public void getCommentsFromGithub() throws ParseException {
//        //given
//        boolean result = false;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date lastDate = dateFormat.parse("2020-11-20 19:38:25");
//
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{" +
//                        "    \"data\": {" +
//                        "        \"repository\": {" +
//                        "            \"defaultBranchRef\": {" +
//                        "                \"target\": {" +
//                        "                    \"history\": {" +
//                        "                        \"totalCount\": 1," +
//                        "                        \"pageInfo\": {" +
//                        "                            \"startCursor\": \"50393dc3a0c59cfefd349d31992256efd6f8c261 0\"" +
//                        "                        }" +
//                        "                    }" +
//                        "                }" +
//                        "            }" +
//                        "        }" +
//                        "    }" +
//                        "}")
//                .addHeader("Content-Type", "application/json")
//        );
//
//        mockWebServer.enqueue(new MockResponse()
//                .setResponseCode(200)
//                .setBody("{\n" +
//                        "    \"data\": {\n" +
//                        "        \"repository\": {\n" +
//                        "            \"defaultBranchRef\": {\n" +
//                        "                \"target\": {\n" +
//                        "                    \"history\": {\n" +
//                        "                        \"nodes\": [\n" +
//                        "                            {\n" +
//                        "                                \"committedDate\": \"2014-10-31T19:39:38Z\",\n" +
//                        "                                \"additions\": 0,\n" +
//                        "                                \"deletions\": 1,\n" +
//                        "                                \"changedFiles\": 1,\n" +
//                        "                                \"author\": {\n" +
//                        "                                    \"email\": \"sebastian@calyptus.eu\",\n" +
//                        "                                    \"name\": \"Sebastian Markbåge\"\n" +
//                        "                                }\n" +
//                        "                            }\n" +
//                        "                        ]\n" +
//                        "                    }\n" +
//                        "                }\n" +
//                        "            }\n" +
//                        "        }\n" +
//                        "    }\n" +
//                        "}").addHeader("Content-Type", "application/json"));
//
//        //when
//        try {
//            result = githubApiService.getCommitsFromGithub("facebook", "react", lastDate);
//        } catch (IOException | InterruptedException e) {
//
//        }
//        Assert.assertTrue(result);
//    }

}