package pvs.app.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;
import pvs.app.dao.GithubPullRequestDAO;
import pvs.app.dto.GithubPullRequestDTO;
import pvs.app.entity.GithubPullRequest;
import pvs.app.entity.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GithubPullRequestServiceTest {

    @Autowired
    private GithubPullRequestService githubPullRequestService;

    @MockBean
    private GithubPullRequestDAO mockGithubPullRequestDAO;

    private List<GithubPullRequest> githubPullRequests;
    private final GithubPullRequest githubPullRequest01 = new GithubPullRequest();
    private final GithubPullRequest githubPullRequest02 = new GithubPullRequest();
    private final GithubPullRequestDTO githubPullRequestDTO01 = new GithubPullRequestDTO();

    @Before
    public void setup() throws ParseException {
        githubPullRequests = new LinkedList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式

        githubPullRequest01.setId(1L);
        githubPullRequest01.setRepoName("react");
        githubPullRequest01.setStatus("open");
        githubPullRequest01.setPullRequestDate(dateFormat.parse("2020-12-19 22:22:22"));

        githubPullRequestDTO01.setStatus("open");
        githubPullRequestDTO01.setPullRequestDate(dateFormat.parse("2020-12-19 22:22:22"));

        githubPullRequest02.setId(2L);
        githubPullRequest02.setRepoName("react");
        githubPullRequest02.setStatus("merged");
        githubPullRequest02.setPullRequestDate(dateFormat.parse("2020-12-21 22:22:22"));

        githubPullRequests.add(githubPullRequest01);
        githubPullRequests.add(githubPullRequest02);
    }
    @Test
    public void getAllPullRequests() {
        //context
        when(mockGithubPullRequestDAO.findByRepositoryId(1L))
                .thenReturn(githubPullRequests);
        //when
        List<GithubPullRequestDTO> githubPullRequestsResult = githubPullRequestService.getAllPullRequests(1L);

        //then
        verify(mockGithubPullRequestDAO, times(1)).findByRepositoryId(1L);
        assertEquals(2, githubPullRequestsResult.size());
    }
}

