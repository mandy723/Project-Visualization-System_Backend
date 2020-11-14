package rvs.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import rvs.demo.model.GithubCommit;
import rvs.demo.model.GithubCommitDTO;
import rvs.demo.repository.GithubCommitDAO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class GithubCommitService {

    private final GithubCommitDAO githubCommitDAO;
    private final ModelMapper modelMapper;

    GithubCommitService(GithubCommitDAO githubCommitDAO, ModelMapper modelMapper) {
        this.githubCommitDAO = githubCommitDAO;
        this.modelMapper = modelMapper;
    }

    public void add(GithubCommitDTO githubCommitDTO) {
        GithubCommit githubCommit = modelMapper.map(githubCommitDTO, GithubCommit.class);
        githubCommitDAO.save(githubCommit);
    }

    public List<GithubCommitDTO> getAllCommits(String repoOwner, String repoName) {
        List<GithubCommit> entities = githubCommitDAO.findByRepoOwnerAndRepoName(repoOwner, repoName);
        List<GithubCommitDTO> githubCommitDTOs = new LinkedList<>();

        for (GithubCommit githubCommit : entities) {
            GithubCommitDTO dto = modelMapper.map(githubCommit, GithubCommitDTO.class);
            dto.setCommittedDate(githubCommit.getCommittedDate());
            githubCommitDTOs.add(dto);
        }
        return githubCommitDTOs;
    }

    public GithubCommitDTO getLastCommit(String repoOwner, String repoName) {
        GithubCommit githubCommit = githubCommitDAO.findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc(repoOwner, repoName);
        GithubCommitDTO githubCommitDTO = modelMapper.map(githubCommit, GithubCommitDTO.class);
        return githubCommitDTO;
    }

    public GithubCommitDTO getCommit(String repoOwner, String repoName, Date committedDate) {
        GithubCommit githubCommit = githubCommitDAO.findByRepoOwnerAndRepoNameAndCommittedDate(repoOwner, repoName, committedDate);
        GithubCommitDTO githubCommitDTO = modelMapper.map(githubCommit, GithubCommitDTO.class);
        return githubCommit != null ? githubCommitDTO: null;
    }

}
