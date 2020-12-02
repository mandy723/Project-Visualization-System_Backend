package rvs.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rvs.demo.model.Member;

import java.util.List;

@Repository
public interface MemberDAO extends CrudRepository<Member, Long> {
    List<Member> findByAccount(String account);
    Member findById(long id);
}
