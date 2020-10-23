package rvs.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private String frontend;

    public void setFrontend(String frontend) {
        this.frontend = frontend;
    }

    public String getFrontend() {
        return frontend;
    }
}