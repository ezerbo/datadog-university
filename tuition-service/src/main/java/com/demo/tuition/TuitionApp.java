package com.demo.tuition;

import com.demo.tuition.config.ServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { ServiceProperties.class })
public class TuitionApp {

	public static void main(String[] args) {
		SpringApplication.run(TuitionApp.class, args);
	}

}
