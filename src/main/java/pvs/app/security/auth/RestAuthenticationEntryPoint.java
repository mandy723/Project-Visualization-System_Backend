//package rvs.demo.security.auth;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Map;
//
//
//public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> map = Map.of("error", "請先登入才能進行此操作");
//        String error = mapper.writeValueAsString(map);
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setStatus(httpServletResponse.SC_UNAUTHORIZED);
//        PrintWriter writer = httpServletResponse.getWriter();
//        writer.write(error);
//        writer.flush();
//        writer.close();
//    }
//}
