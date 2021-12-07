//package pvs.app.service;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import pvs.app.dto.GithubCommentDTO;
//import pvs.app.entity.GithubComment;
//
//@Service
//public class GithubCommentService {
//    private final ModelMapper modelMapper;
//
//    public void save(GithubCommentDTO githubCommentDTO) {
//        GithubComment githubComment = modelMapper.map(githubCommentDTO, GithubComment.class);
//        githubCommentDAO.save(githubComment);
//    }
//}
