package pvs.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pvs.app.dao.GithubCommentDAO;
import pvs.app.dto.GithubCommentDTO;
import pvs.app.entity.GithubComment;

@Service
public class GithubCommentService {
    private final GithubCommentDAO githubCommentDAO;
    private final ModelMapper modelMapper;

    public GithubCommentService(GithubCommentDAO githubCommentDAO, ModelMapper modelMapper) {
        this.githubCommentDAO = githubCommentDAO;
        this.modelMapper = modelMapper;
    }

    public void save(GithubCommentDTO githubCommentDTO) {
        GithubComment githubComment = modelMapper.map(githubCommentDTO, GithubComment.class);
        githubCommentDAO.save(githubComment);
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
