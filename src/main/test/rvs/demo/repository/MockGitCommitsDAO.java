package rvs.demo.repository;

import rvs.demo.model.GitCommit;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MockGitCommitsDAO implements GitCommitDAO {

    private List<GitCommit> gitCommits;

    public MockGitCommitsDAO() {
        gitCommits = new LinkedList<>();
        gitCommits.add(new GitCommit("facebook", "react", new Date()));
        gitCommits.add(new GitCommit("facebook", "react", new Date()));
        gitCommits.add(new GitCommit("facebook", "react", new Date()));
    }

    @Override
    public List<GitCommit> findByOwnerAndName(String owner, String name) {
        return gitCommits;
    }

    @Override
    public GitCommit findFirstByOwnerAndNameOrderByCommittedDateDesc(String owner, String name) {
        return null;
    }

    @Override
    public GitCommit findByOwnerAndNameAndCommittedDate(String owner, String name, Date committedDate) {
        return null;
    }

    @Override
    public <S extends GitCommit> S save(S s) {
        return null;
    }

    @Override
    public <S extends GitCommit> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<GitCommit> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<GitCommit> findAll() {
        return null;
    }

    @Override
    public Iterable<GitCommit> findAllById(Iterable<Long> iterable) {
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
    public void delete(GitCommit gitCommit) {

    }

    @Override
    public void deleteAll(Iterable<? extends GitCommit> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
