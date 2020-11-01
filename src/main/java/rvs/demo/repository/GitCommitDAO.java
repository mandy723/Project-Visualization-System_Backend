package rvs.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rvs.demo.model.GitCommit;

import java.util.Date;
import java.util.List;

@Repository
public interface GitCommitDAO extends CrudRepository<GitCommit, Long> {
    List<GitCommit> findByOwnerAndName(String owner, String name);
    GitCommit findFirstByOwnerAndNameOrderByCommittedDateDesc(String owner, String name);
    GitCommit findByOwnerAndNameAndCommittedDate(String owner, String name, Date committedDate);
}
