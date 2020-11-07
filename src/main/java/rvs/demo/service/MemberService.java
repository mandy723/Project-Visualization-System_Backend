package rvs.demo.service;

import org.springframework.stereotype.Service;
import rvs.demo.model.Member;
import rvs.demo.model.MemberDTO;
import rvs.demo.repository.MemberDAO;

@Service
public class MemberService {

    private final MemberDAO memberDAO;

    MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public void add(MemberDTO bo) {
        Member entity = new Member();
        entity.setAccount(bo.getAccount());
        entity.setPassword(bo.getPassword());
        memberDAO.save(entity);
    }

    public MemberDTO get(long id) {
        Member entity = memberDAO.findById(id);
        MemberDTO bo = new MemberDTO();
        bo.setId(entity.getId());
        bo.setAccount(entity.getAccount());
        bo.setPassword(entity.getPassword());
        return bo;
    }
}
