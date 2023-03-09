package com.demo.enrollment.resource;

import com.demo.enrollment.model.api.*;
import com.demo.enrollment.service.StudentService;
import com.demo.enrollment.model.Student;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Students", description = "Operations on Students")
public class StudentResource {
    
    private final StudentService service;

    public StudentResource(StudentService service) {
        this.service = service;
    }

    @PostMapping(ServicePaths.STUDENTS)
    public ResponseEntity<Student> create(@RequestBody @Valid CreateStudentRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping(ServicePaths.STUDENT_BY_ID)
    public ResponseEntity<Student> update(@PathVariable Long id,
                                          @RequestBody @Valid UpdateStudentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping(ServicePaths.STUDENTS)
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(ServicePaths.STUDENT_BY_ID)
    public ResponseEntity<Student> getOne(@PathVariable Long id) {
        return service.getOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(ServicePaths.STUDENT_BY_ID)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ServicePaths.STUDENT_ENROLLMENTS)
    public ResponseEntity<GetStudentEnrollmentsResponse> getEnrollments(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEnrollments(id));
    }

    @GetMapping(ServicePaths.TUITION)
    public ResponseEntity<Tuition> getTuitionDetails(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTuitionDetails(id));
    }

    @PutMapping(ServicePaths.TUITION)
    public ResponseEntity<Tuition> updateTuitionDetails(@PathVariable Long id,
                                                        @RequestBody @Valid UpdateTuitionRequest request) {
        return ResponseEntity.ok(service.updateTuitionDetails(id, request));
    }


}
