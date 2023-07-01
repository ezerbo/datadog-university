package com.demo.grade.resource;

import com.demo.grade.model.Grade;
import com.demo.grade.model.api.CreateGradeRequest;
import com.demo.grade.model.api.ServicePaths;
import com.demo.grade.model.api.UpdateGradeRequest;
import com.demo.grade.service.GradeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Tag(name = "Grades", description = "Operations on Grades")
public class GradeResource {

    private final GradeService service;

    public GradeResource(GradeService service) {
        this.service = service;
    }

    @PostMapping(ServicePaths.GRADES)
    public ResponseEntity<Grade> create(@RequestBody @Valid CreateGradeRequest request) {
        log.info("Creating a new grade: {}", request);
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping(ServicePaths.GRADE_BY_ID)
    public ResponseEntity<Grade> update(@PathVariable Long id, @RequestBody @Valid UpdateGradeRequest request) {
        log.info("Updating grade with id: '{}', '{}'", id, request);
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping(ServicePaths.GRADE_BY_ID)
    public ResponseEntity<Grade> get(@PathVariable Long id, @RequestParam Long tuitionId) {
        log.info("Getting grades with id: '{}', tuitionId: {}", id, tuitionId);
        return ResponseEntity.ok(service.get(id, tuitionId));
    }

    @DeleteMapping(ServicePaths.GRADE_BY_ID)
    public ResponseEntity<Grade> delete(@PathVariable Long id) {
        log.info("Deleting grade with id: '{}'", id);
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
