package com.demo.grade.service.client;

import com.demo.grade.config.ServiceProperties;
import com.demo.grade.model.api.Tuition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    public boolean isTuitionPaid(Long tuitionId) {
        log.info("Getting tuition with id: {}", tuitionId);
        ResponseEntity<Tuition> response = client.getForEntity(tuitionApiUrl.concat("/{id}"),
                Tuition.class, tuitionId);
        log.info("Tuition API response: {}", response);
        return response.getStatusCode().equals(HttpStatus.OK) && response.getBody().isPaid();
    }
}
