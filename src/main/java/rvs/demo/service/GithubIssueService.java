package rvs.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import rvs.demo.model.GithubIssue;
import rvs.demo.model.GithubIssueDTO;
import rvs.demo.repository.GithubIssueDAO;

@Service
public class GithubIssueService {
    private final GithubIssueDAO githubIssueDAO;
    private final ModelMapper modelMapper;

    GithubIssueService(GithubIssueDAO githubIssueDAO, ModelMapper modelMapper) {
        this.githubIssueDAO = githubIssueDAO;
        this.modelMapper = modelMapper;
    }

    public void add(GithubIssueDTO githubIssueDTO) {
        GithubIssue githubIssue = modelMapper.map(githubIssueDTO, GithubIssue.class);
        githubIssueDAO.save(githubIssue);
    }
}
