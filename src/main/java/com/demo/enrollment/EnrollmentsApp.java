package com.demo.enrollment;

import com.demo.enrollment.config.ServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { ServiceProperties.class })
public class EnrollmentsApp {

    public static void main(String[] args) {
        SpringApplication.run(EnrollmentsApp.class, args);
    }

}
