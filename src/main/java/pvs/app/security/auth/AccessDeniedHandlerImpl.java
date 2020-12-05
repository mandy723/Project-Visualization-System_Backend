//package rvs.demo.security.auth;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Map;
//
//public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
//            throws IOException, ServletException {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> result = Map.of("message", "你無權限可執行該動作!");
//        response.setContentType("application/json;charset=UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        PrintWriter out = response.getWriter();
//        out.write(mapper.writeValueAsString(result));
//        out.flush();
//        out.close();
//    }
//}
