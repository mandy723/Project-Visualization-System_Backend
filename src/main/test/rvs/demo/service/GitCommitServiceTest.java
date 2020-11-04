package rvs.demo.service;

import org.junit.Test;
import rvs.demo.repository.GitCommitDAO;
import rvs.demo.repository.MockGitCommitsDAO;

import static org.junit.Assert.assertEquals;

public class GitCommitServiceTest {

    @Test
    public void get_all_commits() {
        GitCommitDAO gitCommitDAO = new MockGitCommitsDAO();
        GitCommitService gitCommitService = new GitCommitService(gitCommitDAO);
        assertEquals(3, gitCommitService.getAllCommits("facebook", "react").size());
    }

}
