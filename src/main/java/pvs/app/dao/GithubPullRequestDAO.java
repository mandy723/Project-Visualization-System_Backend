package pvs.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pvs.app.entity.GithubPullRequest;

import java.util.List;

@Repository
public interface GithubPullRequestDAO extends CrudRepository<GithubPullRequest, Long> {
    List<GithubPullRequest>findByRepositoryId(Long repoId);
}