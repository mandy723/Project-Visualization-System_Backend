package rvs.demo.service;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import rvs.demo.repository.GithubCommitDAO;
import rvs.demo.repository.MockGithubCommitsDAO;

import static org.junit.Assert.assertEquals;

public class GithubCommitServiceTest {

    @Test
    public void get_all_commits() {
        ModelMapper modelMapper = new ModelMapper();
        GithubCommitDAO githubCommitDAO = new MockGithubCommitsDAO();
        GithubCommitService githubCommitService = new GithubCommitService(githubCommitDAO, modelMapper);
        assertEquals(1, githubCommitService.getAllCommits("facebook", "react").size());
    }

}
