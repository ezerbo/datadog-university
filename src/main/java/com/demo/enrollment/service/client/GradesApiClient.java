package com.demo.enrollment.service.client;

import com.demo.enrollment.config.ExternalServiceConfig;
import com.demo.enrollment.config.ServiceProperties;
import com.demo.enrollment.model.Enrollment;
import com.demo.enrollment.model.api.CreateGradeRequest;
import com.demo.enrollment.model.api.Grade;
import com.demo.enrollment.model.api.UpdateGradeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GradesApiClient {

    private final ExternalServiceConfig gradesServiceConfig;
    private final RestTemplate client = new RestTemplate();

    public GradesApiClient(ServiceProperties properties) {
        this.gradesServiceConfig = properties.getGradesServiceConfig();
    }

    public Grade get(Enrollment enrollment) {
        log.info("Getting grade for enrollment '{}'", enrollment);
        // TODO Add service check
        return client.getForEntity(String.format(gradesServiceConfig.getUrl().concat("/%s"),
                        enrollment.getGradeId()), Grade.class)
                .getBody();
    }

    public Grade create(CreateGradeRequest request) {
        log.info("Creating grade {}", request);
        HttpEntity<CreateGradeRequest> entity = new HttpEntity<>(request);
        return client.exchange(gradesServiceConfig.getUrl(), HttpMethod.POST, entity, Grade.class)
                .getBody();
    }

    public Grade update(Long gradeId, UpdateGradeRequest request) {
        log.info("updating grade {}", request);
        HttpEntity<UpdateGradeRequest> entity = new HttpEntity<>(request);
        return client.exchange(String.format(gradesServiceConfig.getUrl().concat("/%s"), gradeId),
                        HttpMethod.PUT, entity, Grade.class)
                .getBody();
    }

}
