package pvs.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pvs.app.dao.GithubCommentDAO;
import pvs.app.dao.RepositoryDAO;
import pvs.app.dto.GithubCommentDTO;
import pvs.app.entity.GithubComment;
import pvs.app.entity.Repository;

import java.util.LinkedList;
import java.util.List;

@Service
public class GithubCommentService {
    private final GithubCommentDAO githubCommentDAO;
    private final RepositoryDAO repositoryDAO;
    private final ModelMapper modelMapper;

    public GithubCommentService(GithubCommentDAO githubCommentDAO,RepositoryDAO repositoryDAO, ModelMapper modelMapper) {
        this.githubCommentDAO = githubCommentDAO;
        this.repositoryDAO = repositoryDAO;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(GithubCommentDTO githubCommentDTO) {
        GithubComment githubComment = modelMapper.map(githubCommentDTO, GithubComment.class);
        String repoUrl = "https://github.com/" + githubCommentDTO.getRepoOwner() + "/" + githubCommentDTO.getRepoName();
        Repository repository = repositoryDAO.findByUrl(repoUrl);
        repository.getGithubCommentSet().add(githubComment);
        githubCommentDAO.save(githubComment);
        repositoryDAO.save(repository);
    }

    public List<GithubCommentDTO> getAllComments(String repoOwner, String repoName) {
        List<GithubComment> entities = githubCommentDAO.findByRepoOwnerAndRepoName(repoOwner, repoName);
        List<GithubCommentDTO> githubCommentDTOs = new LinkedList<>();

        for (GithubComment githubComment : entities) {
            GithubCommentDTO dto = modelMapper.map(githubComment, GithubCommentDTO.class);
            dto.setCreatedAt(githubComment.getCreatedAt());
            githubCommentDTOs.add(dto);
        }
        return githubCommentDTOs;
    }

    public GithubCommentDTO getLastComment(String repoOwner, String repoName) {
        GithubComment githubComment = githubCommentDAO.findFirstByRepoOwnerAndRepoNameOrderByCreatedAtDesc(repoOwner, repoName);
        if(null == githubComment) {
            return null;
        }
        GithubCommentDTO dto = modelMapper.map(githubComment, GithubCommentDTO.class);
        dto.setCreatedAt(githubComment.getCreatedAt());
        return dto;
    }

}
