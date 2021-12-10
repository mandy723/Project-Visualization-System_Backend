package pvs.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pvs.app.entity.GithubComment;

import java.util.List;

@Repository
public interface GithubCommentDAO extends CrudRepository<GithubComment, Long>  {
    List<GithubComment> findByRepoOwnerAndRepoName(String repoOwner, String repoName);
    GithubComment findFirstByRepoOwnerAndRepoNameOrderByCreatedAtDesc(String repoOwner, String repoName);
}
