package com.demo.enrollment.service.client;

import com.demo.enrollment.config.ExternalServiceConfig;
import com.demo.enrollment.config.ServiceProperties;
import com.demo.enrollment.model.api.CreateTuitionRequest;
import com.demo.enrollment.model.api.SetTuitionRequest;
import com.demo.enrollment.model.api.Tuition;
import com.demo.enrollment.model.api.UpdateTuitionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class TuitionApiClient {

    private final String tuitionApiUrl;

    private final RestTemplate client = new RestTemplate();

    public TuitionApiClient(ServiceProperties properties) {
        this.tuitionApiUrl = properties.getTuitionServiceConfig()
                .getUrl();
    }

    public Tuition create(CreateTuitionRequest request) {
        log.info("Creating a new tuition record: {}", request);
        HttpEntity<CreateTuitionRequest> entity = new HttpEntity<>(request);
        ResponseEntity<Tuition> response = client.exchange(tuitionApiUrl, HttpMethod.POST, entity, Tuition.class);
        log.info("Tuition creation response: {}", response);
        return response.getBody();
    }

    public Tuition update(Long tuitionId, SetTuitionRequest request) {
        log.info("Updating a tuition record: {}", request);
        HttpEntity<SetTuitionRequest> entity = new HttpEntity<>(request);
        ResponseEntity<Tuition> response = client.exchange(tuitionApiUrl.concat("/{id}"), HttpMethod.PUT, entity,
                Tuition.class, tuitionId);
        log.info("Tuition update response: {}", response);
        return response.getBody();
    }

    public Tuition get(Long tuitionId) {
        log.info("Getting tuition with id: {}", tuitionId);
        ResponseEntity<Tuition> response = client.getForEntity(tuitionApiUrl.concat("/{id}"), Tuition.class, tuitionId);
        log.info("Tuition API response: {}", response);
        return response.getBody();
    }

    public void delete(Long tuitionId) {
        log.info("Deleting tuition with id: {}", tuitionId);
        client.delete(tuitionApiUrl.concat("/{id}"), tuitionId);
        log.info("Tuition record successfully deleted");
    }
}
