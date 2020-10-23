package rvs.demo.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberDAO extends CrudRepository<Member, Long> {
    List<Member> findByAccount(String account);
    Member findById(long id);
}
