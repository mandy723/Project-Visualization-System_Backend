package rvs.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rvs.demo.model.GitCommit;
import rvs.demo.model.GitCommitBo;
import rvs.demo.repository.GitCommitDAO;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class GitCommitService {

    @Autowired
    private GitCommitDAO gitCommitDAO;

    public void add(GitCommitBo bo) {
        GitCommit entity = new GitCommit();
        entity.setOwner(bo.getOwner());
        entity.setName(bo.getName());
        entity.setCommittedDate(bo.getCommittedDate());
        gitCommitDAO.save(entity);
    }

    public List<GitCommitBo> getAll(String owner, String name) {
        List<GitCommit> entities = gitCommitDAO.findByOwnerAndName(owner, name);
        List<GitCommitBo> bos = new LinkedList<>();

        for (GitCommit entity : entities) {
            GitCommitBo bo = new GitCommitBo(entity.getOwner(), entity.getName(), entity.getCommittedDate());
            bos.add(bo);
        }
        return bos;
    }

    public GitCommitBo getLastCommit(String owner, String name) {
        GitCommit entity = gitCommitDAO.findFirstByOwnerAndNameOrderByCommittedDateDesc(owner, name);
        return new GitCommitBo(entity.getOwner(), entity.getName(), entity.getCommittedDate());
    }

    public GitCommitBo getCommit(String owner, String name, Date committedDate) {
        GitCommit entity = gitCommitDAO.findByOwnerAndNameAndCommittedDate(owner, name, committedDate);
        return new GitCommitBo(entity.getOwner(), entity.getName(), entity.getCommittedDate());
    }

}
