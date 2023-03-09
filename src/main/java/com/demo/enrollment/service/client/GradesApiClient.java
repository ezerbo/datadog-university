package com.demo.enrollment.service.client;

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

    private final String gradesApiUrl;
    private final RestTemplate client = new RestTemplate();

    public GradesApiClient(ServiceProperties properties) {
        this.gradesApiUrl = properties.getGradesServiceConfig()
                .getUrl();
    }

    public Grade get(Enrollment enrollment) {
        log.info("Getting grade for enrollment '{}'", enrollment);
        // TODO Add service check
        return client.getForEntity(gradesApiUrl.concat("/{id}?tuitionId={tuitionId}"), Grade.class,
                        enrollment.getGradeId(), enrollment.getStudent().getTuitionId())
                .getBody();
    }

    public Grade create(CreateGradeRequest request) {
        log.info("Creating grade {}", request);
        HttpEntity<CreateGradeRequest> entity = new HttpEntity<>(request);
        return client.exchange(gradesApiUrl, HttpMethod.POST, entity, Grade.class).getBody();
    }

    public Grade update(Long gradeId, UpdateGradeRequest request) {
        log.info("Updating grade {}", request);
        HttpEntity<UpdateGradeRequest> entity = new HttpEntity<>(request);
        return client.exchange(gradesApiUrl.concat("/{id}"), HttpMethod.PUT, entity, Grade.class, gradeId)
                .getBody();
    }

}
