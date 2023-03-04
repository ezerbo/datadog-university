package com.demo.enrollment.resource;

import com.demo.enrollment.model.api.*;
import com.demo.enrollment.service.CourseService;
import com.demo.enrollment.model.Course;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Courses", description = "Operations on Courses")
public class CourseResource {

    private final CourseService service;

    public CourseResource(CourseService service) {
        this.service = service;
    }

    @PostMapping(ServicePaths.COURSES)
    public ResponseEntity<Course> create(@RequestBody @Valid CreateOrUpdateCourseRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping(ServicePaths.COURSE_BY_ID)
    public ResponseEntity<Course> update(@PathVariable Long id,
                                         @RequestBody @Valid CreateOrUpdateCourseRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping(ServicePaths.COURSES)
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping(ServicePaths.COURSE_BY_ID)
    public ResponseEntity<Course> getOne(@PathVariable Long id) {
        return service.getOne(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(ServicePaths.COURSE_BY_ID)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ServicePaths.COURSE_ENROLLMENTS)
    public ResponseEntity<GetCourseEnrollmentsResponse> getEnrollments(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEnrollments(id));
    }

    @PostMapping(ServicePaths.COURSE_ENROLLMENTS)
    public ResponseEntity<CourseEnrollmentResponse> enroll(@PathVariable Long id,
                                                           @RequestBody CourseEnrollmentRequest request) {
        return ResponseEntity.ok(service.enroll(id, request));
    }
}

