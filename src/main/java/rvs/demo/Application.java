package rvs.demo;

import rvs.demo.config.YAMLConfig;
import rvs.demo.controller.ProductController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("rvs.demo")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}