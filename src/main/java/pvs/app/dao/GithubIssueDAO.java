package pvs.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pvs.app.entity.GithubIssue;

import java.util.List;

@Repository
public interface GithubIssueDAO extends CrudRepository<GithubIssue, Long> {
    List<GithubIssue> findByRepoOwnerAndRepoName(String repoOwner, String repoName);
}
