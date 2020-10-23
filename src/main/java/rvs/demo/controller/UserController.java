package rvs.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @GetMapping("/admin/hello")
    public Map<String, String> adminSayHello() {
        Map<String, String> result = Map.of("message", "admin say hello");
        return result;
    }

    @GetMapping("/user/hello")
    public Map<String, String> userSayHello() {
        Map<String, String> result = Map.of("message", "user say hello");
        return result;
    }
}
