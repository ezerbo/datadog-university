package com.demo.grade.service;

import com.demo.grade.config.ExternalServiceConfig;
import com.demo.grade.config.ServiceProperties;
import com.demo.grade.model.Course;
import com.demo.grade.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
public class EnrollmentsApiClient {

    private final RestTemplate client = new RestTemplate();
    private final ExternalServiceConfig enrollmentsServiceConfig;

    public EnrollmentsApiClient(ServiceProperties properties) {
        this.enrollmentsServiceConfig = properties.getEnrollmentsServiceConfig();
    }

    public boolean isValidStudent(Long studentId) {
      log.info("Checking if student with id '{}' is valid", studentId);
        ResponseEntity<Student> response = client.exchange(String.format(enrollmentsServiceConfig.getUrl()
                        .concat("/students/%s"), studentId),
                HttpMethod.GET, null, Student.class);
        log.info("Student validation response: {}", response);
        return Objects.nonNull(response.getBody()) && response.getStatusCode().equals(HttpStatus.OK);
    }

    public boolean isValidCourse(Long courseId) {
        log.info("Checking if course with id '{}' is valid", courseId);
        ResponseEntity<Course> response = client.exchange(String.format(enrollmentsServiceConfig.getUrl()
                        .concat("/courses/%s"), courseId),
                HttpMethod.GET, null, Course.class);
        log.info("Course validation response: {}", response);
        return Objects.nonNull(response.getBody()) && response.getStatusCode().equals(HttpStatus.OK);
    }

}
