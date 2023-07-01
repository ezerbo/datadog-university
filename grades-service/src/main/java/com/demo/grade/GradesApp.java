package com.demo.grade;

import com.demo.grade.config.ServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { ServiceProperties.class })
public class GradesApp {

	public static void main(String[] args) {
		SpringApplication.run(GradesApp.class, args);
	}

}
