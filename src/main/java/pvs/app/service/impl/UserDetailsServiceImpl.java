package pvs.app.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pvs.app.dao.MemberDAO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberDAO memberDAO;

    UserDetailsServiceImpl(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //查数据库
        return memberDAO.findByUsername( userName );
    }
}
