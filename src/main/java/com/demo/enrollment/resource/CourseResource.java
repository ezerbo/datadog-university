package com.demo.enrollment.resource;

import com.demo.enrollment.model.api.*;
import com.demo.enrollment.service.CourseService;
import com.demo.enrollment.model.Course;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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

    @GetMapping(ServicePaths.GRADES)
    public ResponseEntity<GradeDTO> getGrade(@PathVariable Long id, @RequestParam Long studentId) {
        log.info("Getting student grades of student with id: '{}' and courseId: {}", studentId, id);
        return ResponseEntity.ok(service.getGrade(id, studentId));
    }

    @PostMapping(ServicePaths.GRADES)
    public ResponseEntity<GradeDTO> setGrade(@PathVariable Long id, @RequestBody @Valid SetGradeRequest request) {
        log.info("Setting grade for course with id: {}, {}", id, request);
        return ResponseEntity.ok(service.setGrade(id, request));
    }
}

