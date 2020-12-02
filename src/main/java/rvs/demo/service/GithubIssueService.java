package rvs.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import rvs.demo.model.GithubIssue;
import rvs.demo.dto.GithubIssueDTO;
import rvs.demo.dao.GithubIssueDAO;

import java.util.LinkedList;
import java.util.List;

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

    public List<GithubIssueDTO> getAllIssues(String repoOwner, String repoName) {
        List<GithubIssue> entities = githubIssueDAO.findByRepoOwnerAndRepoName(repoOwner, repoName);
        List<GithubIssueDTO> githubIssueDTOs = new LinkedList<>();

        for (GithubIssue githubIssue : entities) {
            GithubIssueDTO dto = modelMapper.map(githubIssue, GithubIssueDTO.class);
            dto.setCreatedAt(githubIssue.getCreatedAt());
            dto.setClosedAt(githubIssue.getClosedAt());
            githubIssueDTOs.add(dto);
        }

        return githubIssueDTOs;
    }
}
