package pvs.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pvs.app.dao.RepositoryDAO;
import pvs.app.entity.GithubCommit;
import pvs.app.entity.Repository;
import pvs.app.dto.GithubCommitDTO;
import pvs.app.dao.GithubCommitDAO;
import java.util.LinkedList;
import java.util.List;

@Service
public class GithubCommitService {

    private final GithubCommitDAO githubCommitDAO;
    private final RepositoryDAO repositoryDAO;
    private final ModelMapper modelMapper;

    GithubCommitService(GithubCommitDAO githubCommitDAO,RepositoryDAO repositoryDAO, ModelMapper modelMapper) {
        this.githubCommitDAO = githubCommitDAO;
        this.repositoryDAO = repositoryDAO;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(GithubCommitDTO githubCommitDTO) {
        GithubCommit githubCommit = modelMapper.map(githubCommitDTO, GithubCommit.class);
        String repoUrl = "https://github.com/" + githubCommitDTO.getRepoOwner() + "/" + githubCommitDTO.getRepoName();
        Repository repository = repositoryDAO.findByUrl(repoUrl);
        githubCommitDAO.save(githubCommit);
        repository.getGithubCommitSet().add(githubCommit);
        repositoryDAO.save(repository);
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
        if(null == githubCommit) {
            return null;
        }
        GithubCommitDTO dto = modelMapper.map(githubCommit, GithubCommitDTO.class);
        dto.setCommittedDate(githubCommit.getCommittedDate());
        return dto;
    }
}
