package rvs.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rvs.demo.model.Member;
import rvs.demo.model.MemberBo;
import rvs.demo.repository.MemberDAO;

@Service
public class MemberService {

    @Autowired
    private MemberDAO memberDAO;

    public void add(MemberBo bo) {
        Member entity = new Member();
        entity.setAccount(bo.getAccount());
        entity.setPassword(bo.getPassword());
        memberDAO.save(entity);
    }

    public MemberBo get(long id) {
        Member entity = memberDAO.findById(id);
        MemberBo bo = new MemberBo();
        bo.setId(entity.getId());
        bo.setAccount(entity.getAccount());
        bo.setPassword(entity.getPassword());
        return bo;
    }
}
