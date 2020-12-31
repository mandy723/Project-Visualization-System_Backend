//package pvs.app.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.internal.runners.statements.Fail;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.reactive.function.client.WebClient;
//import pvs.app.Application;
//import pvs.app.dto.GithubCommitDTO;
//import pvs.app.dto.GithubIssueDTO;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//public class GithubApiServiceTest {
//    private MockWebServer mockWebServer;
//
//    private GithubApiService githubApiService;
//
//    @MockBean
//    private GithubCommitService githubCommitService;
//
//    @Before
//    public void setUp() throws Exception {
//        this.mockWebServer = new MockWebServer();
//        this.mockWebServer.start();
//        this.githubApiService = new GithubApiService(WebClient.builder(), mockWebServer.url("/").toString(), githubCommitService);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        this.mockWebServer.close();
//    }
//
//    @Test
//    public void getAvatarURL() {
//        MockResponse mockResponse = new MockResponse()
//                .addHeader("Content-Type", "application/json; charset=utf-8")
//                .setBody("{\"data\": {\"search\": {\"edges\": [{\"node\": {\"avatarUrl\": \"https://www.peppercarrot.com/extras/html/2016_cat-generator/avatar.php?seed=5fedb63229876\"}}]}}}")
//                .setResponseCode(200);
//
//        mockWebServer.enqueue(mockResponse);
//
//        try {
//            JsonNode avatar = this.githubApiService.getAvatarURL("imper0502");
//            assertEquals("https://www.peppercarrot.com/extras/html/2016_cat-generator/avatar.php?seed=5fedb63229876", avatar.textValue());
//        } catch (IOException e) {
//            fail();
//        }
//    }
//
//    @Test
//    public void getIssueFromGithub() {
//        MockResponse mockResponse = new MockResponse()
//                .addHeader("Content-Type", "application/json; charset=utf-8")
//                .setBody("{\"data\": {\"repository\": {\"issues\": {\"totalCount\": 1}}}}")
//                .setResponseCode(200);
//
//        mockWebServer.enqueue(mockResponse);
//
//
//        try {
//            List<GithubIssueDTO> githubIssueDTOList = this.githubApiService.getIssuesFromGithub("imper0502", "bopomo");
//
//        } catch (IOException e) {
//            fail();
//        } catch (InterruptedException e) {
//            fail();
//        }
//    }
//}