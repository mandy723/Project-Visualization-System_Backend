package rvs.demo.repository;

import org.springframework.data.repository.CrudRepository;
import rvs.demo.model.Member;
import rvs.demo.model.Repository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface ProjectDAO extends CrudRepository<Repository, Long> {
    List<Member> findByProjectID(Long projectID);
    Repository findById(long id);
}
