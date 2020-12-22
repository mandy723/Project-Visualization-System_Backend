package pvs.app.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;
import pvs.app.entity.GithubCommit;
import pvs.app.dto.GithubCommitDTO;
import pvs.app.dao.GithubCommitDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GithubCommitServiceTest {

    @Autowired
    private GithubCommitService githubCommitService;

    @MockBean
    private GithubCommitDAO githubCommitDAO;

    private List<GithubCommit> mockGithubCommits;
    private final GithubCommit githubCommit01 = new GithubCommit();
    private final GithubCommit githubCommit02 = new GithubCommit();
    private final GithubCommitDTO githubCommitDTO01 = new GithubCommitDTO();


    @Before
    public void init() throws ParseException {
        mockGithubCommits = new LinkedList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式

        githubCommit01.setRepoOwner("facebook");
        githubCommit01.setRepoName("react");
        githubCommit01.setCommittedDate(dateFormat.parse("2020-12-29 11:11:11"));

        githubCommitDTO01.setRepoName("facebook");
        githubCommitDTO01.setRepoName("react");
        githubCommitDTO01.setCommittedDate(dateFormat.parse("2020-12-29 11:11:11"));

        githubCommit02.setRepoOwner("facebook");
        githubCommit02.setRepoName("react");
        githubCommit02.setCommittedDate(dateFormat.parse("2020-12-30 11:11:11"));

        mockGithubCommits.add(githubCommit01);
        mockGithubCommits.add(githubCommit02);
    }

    @Test
    public void get_all_commits() {
        //when
        when(githubCommitDAO.findByRepoOwnerAndRepoName("facebook", "react"))
                .thenReturn(mockGithubCommits);

        List<GithubCommitDTO> githubCommits = githubCommitService.getAllCommits("facebook", "react");

        //expect
        assertEquals(2, githubCommits.size());
        verify(githubCommitDAO, times(1)).findByRepoOwnerAndRepoName("facebook", "react");
    }

    @Test
    public void get_last_commit() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式

        //when
        when(githubCommitDAO.findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("facebook", "react"))
                .thenReturn(mockGithubCommits.get(1));

        GithubCommitDTO githubCommit = githubCommitService.getLastCommit("facebook", "react");

        //expect
        assertEquals(dateFormat.parse("2020-12-30 11:11:11"), githubCommit.getCommittedDate());
        verify(githubCommitDAO, times(1)).findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc("facebook", "react");

    }
}
