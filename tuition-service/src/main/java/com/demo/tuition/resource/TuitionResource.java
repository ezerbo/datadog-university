package com.demo.tuition.resource;

import com.demo.tuition.model.Tuition;
import com.demo.tuition.model.api.CreateTuitionRequest;
import com.demo.tuition.model.api.ServicePaths;
import com.demo.tuition.model.api.UpdateTuitionRequest;
import com.demo.tuition.service.TuitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class TuitionResource {

    private final TuitionService service;

    public TuitionResource(TuitionService service) {
        this.service = service;
    }

    @PostMapping(ServicePaths.TUITION)
    public ResponseEntity<Tuition> create(@RequestBody @Valid CreateTuitionRequest request) {
        log.info("Creating a new tuition record: {}", request);
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping(ServicePaths.TUITION_BY_ID)
    public ResponseEntity<Tuition> update(@PathVariable Long id, @RequestBody @Valid UpdateTuitionRequest request) {
        log.info("Updating tuition record with id: {}, {}", id, request);
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping(ServicePaths.TUITION_BY_ID)
    public ResponseEntity<Tuition> get(@PathVariable Long id) {
        log.info("Getting tuition record with id: {}", id);
        return ResponseEntity.ok(service.get(id));
    }

    @DeleteMapping(ServicePaths.TUITION_BY_ID)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting tuition record with id: {}", id);
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
