package rvs.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rvs.demo.model.GithubIssue;

import java.util.List;

@Repository
public interface GithubIssueDAO extends CrudRepository<GithubIssue, Long> {
    List<GithubIssue> findByRepoOwnerAndRepoName(String repoOwner, String repoName);
}
