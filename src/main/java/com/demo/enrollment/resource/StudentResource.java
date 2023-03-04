package com.demo.enrollment.resource;

import com.demo.enrollment.service.StudentService;
import com.demo.enrollment.model.Student;
import com.demo.enrollment.model.api.CreateOrUpdateStudentRequest;
import com.demo.enrollment.model.api.GetStudentEnrollmentsResponse;
import com.demo.enrollment.model.api.ServicePaths;
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
    public ResponseEntity<Student> create(@RequestBody @Valid CreateOrUpdateStudentRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping(ServicePaths.STUDENT_BY_ID)
    public ResponseEntity<Student> update(@PathVariable Long id,
                                          @RequestBody @Valid CreateOrUpdateStudentRequest request) {
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
}
