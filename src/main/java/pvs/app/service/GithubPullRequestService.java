//package pvs.app.service;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import pvs.app.dao.GithubPullRequestDAO;
//import pvs.app.dto.GithubPullRequestDTO;
//import pvs.app.entity.GithubPullRequest;
//import pvs.app.entity.Repository;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@Service
//public class GithubPullRequestService {
//
//    private final GithubPullRequestDAO githubPullRequestDAO;
//    private final ModelMapper modelMapper;
//
//    GithubPullRequestService(GithubPullRequestDAO githubPullRequestDAO, ModelMapper modelMapper) {
//        this.githubPullRequestDAO = githubPullRequestDAO;
//        this.modelMapper = modelMapper;
//    }
//
//    public void save(GithubPullRequestDTO githubPullRequestDTO) {
//        GithubPullRequest githubPullRequest = modelMapper.map(githubPullRequestDTO, GithubPullRequest.class);
//        githubPullRequestDAO.save(githubPullRequest);
//    }
//
//    public List<GithubPullRequestDTO> getAllPullRequests(Repository repo) {
//        List<GithubPullRequest> entities = githubPullRequestDAO.findByRepository(repo);
//        List<GithubPullRequestDTO> githubPullRequestDTOs = new LinkedList<>();
//
//        for (GithubPullRequest githubPullRequest : entities) {
//            GithubPullRequestDTO dto = modelMapper.map(githubPullRequest, GithubPullRequestDTO.class);
//            dto.setPullRequestDate(githubPullRequest.getPullRequestDate());
//            githubPullRequestDTOs.add(dto);
//        }
//        return githubPullRequestDTOs;
//    }
//}
