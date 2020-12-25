package pvs.app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pvs.app.filter.JwtTokenFilter;
import pvs.app.utils.JwtTokenUtil;

@Service
public class AuthService {

    static final Logger logger = LogManager.getLogger(AuthService.class.getName());

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private JwtTokenUtil jwtTokenUtil;

    AuthService(PasswordEncoder passwordEncoder,
                AuthenticationManager authenticationManager,
                @Qualifier("userDetailsServiceImpl")UserDetailsService userDetailsService,
                JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String login(String username, String password) {
        logger.debug("username "+username+ ",password: "+password);
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken( username, password );
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername( username );
        String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }
}
