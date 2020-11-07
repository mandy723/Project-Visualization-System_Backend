package rvs.demo.repository;

import rvs.demo.model.GithubCommit;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MockGithubCommitsDAO implements GithubCommitDAO {

    private List<GithubCommit> githubCommits;

    public MockGithubCommitsDAO() {
        githubCommits = new LinkedList<>();
        GithubCommit githubCommit = new GithubCommit();
        githubCommit.setRepoOwner("facebook");
        githubCommit.setRepoName("react");
        githubCommit.setCommittedDate(new Date());
        githubCommits.add(githubCommit);
    }

    @Override
    public List<GithubCommit> findByRepoOwnerAndRepoName(String repoOwner, String repoName) {
        return githubCommits;
    }

    @Override
    public GithubCommit findFirstByRepoOwnerAndRepoNameOrderByCommittedDateDesc(String repoOwner, String repoName) {
        return null;
    }

    @Override
    public GithubCommit findByRepoOwnerAndRepoNameAndCommittedDate(String repoOwner, String repoName, Date committedDate) {
        return null;
    }

    @Override
    public <S extends GithubCommit> S save(S s) {
        return null;
    }

    @Override
    public <S extends GithubCommit> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<GithubCommit> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<GithubCommit> findAll() {
        return null;
    }

    @Override
    public Iterable<GithubCommit> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(GithubCommit githubCommit) {

    }

    @Override
    public void deleteAll(Iterable<? extends GithubCommit> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
