package com.demo.enrollment.service;

import com.demo.enrollment.error.DuplicateRecordException;
import com.demo.enrollment.error.InvalidCourseDatesException;
import com.demo.enrollment.error.NoDataFoundException;
import com.demo.enrollment.model.Enrollment;
import com.demo.enrollment.model.EnrollmentId;
import com.demo.enrollment.model.Course;
import com.demo.enrollment.model.Student;
import com.demo.enrollment.model.api.CourseEnrollmentRequest;
import com.demo.enrollment.model.api.CourseEnrollmentResponse;
import com.demo.enrollment.model.api.CreateOrUpdateCourseRequest;
import com.demo.enrollment.model.api.GetCourseEnrollmentsResponse;
import com.demo.enrollment.repository.EnrollmentRepository;
import com.demo.enrollment.repository.CourseRepository;
import com.demo.enrollment.repository.StudentRepository;
import datadog.trace.api.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository repository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository repository,
                         StudentRepository studentRepository,
                         EnrollmentRepository enrollmentRepository) {
        this.repository = repository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }

    @Trace(operationName = "course.create")
    public Course create(CreateOrUpdateCourseRequest request) {
        log.info("Creating a new course: {}", request);
        if (repository.existsByName(request.getName())) {
            log.error("A course with the same name already exists");
            throw new DuplicateRecordException("A course with name: '%s' already exists", request.getName());
        }
        if (request.getStartDate().after(request.getEndDate())) {
            log.error("The start date cannot be after the end date");
            throw new InvalidCourseDatesException("The start date cannot be after the end date");
        }
        Course course = Course.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        return repository.save(course);
    }

    @Trace(operationName = "course.update")
    public Course update(Long id, CreateOrUpdateCourseRequest request) {
        log.info("Updating a course, request: {}, id: {}", request, id);
        Optional<Course> courseOp = repository.findById(id);
        if (!courseOp.isPresent()) {
            log.error("Attempting to update a missing course");
            throw new NoDataFoundException("No course found with id: %s", id);
        }

        if (!courseOp.get().getName().equals(request.getName()) && repository.existsByName(request.getName())) {
            log.error("A course with name '{}' already exists", request.getName());
            throw new DuplicateRecordException("A course with name '%s' already exists", request.getName());
        }
        Course course = Course.builder()
                .id(id)
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .enrollments(courseOp.get().getEnrollments())
                .build();
        return repository.save(course);
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public Optional<Course> getOne(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Trace(operationName = "course.getEnrollments")
    public GetCourseEnrollmentsResponse getEnrollments(Long id) {
        log.info("Getting enrollments for course with id: {}", id);
        return repository.findById(id)
                .map(course -> GetCourseEnrollmentsResponse.builder()
                        .course(course)
                        .students(course.getEnrollments().stream().map(Enrollment::getStudent).collect(Collectors.toList()))
                        .build())
                .orElseThrow(() -> new NoDataFoundException("No student found with id %s", id));
    }

    @Trace(operationName = "course.enroll")
    public CourseEnrollmentResponse enroll(Long courseId, CourseEnrollmentRequest request) {
        log.info("Course enrollment, request: {}", request);
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NoDataFoundException("No student found with id: %s", request.getStudentId()));
        Course course = repository.findById(courseId)
                .orElseThrow(() -> new NoDataFoundException("No course found with id: %s", courseId));
        Enrollment enrollment = Enrollment.builder()
                .id(EnrollmentId.builder()
                        .courseId(courseId)
                        .studentId(request.getStudentId())
                        .build())
                .enrollmentDate(new Date())
                .course(course)
                .student(student)
                .build();
        enrollmentRepository.save(enrollment);
        return CourseEnrollmentResponse.builder()
                .enrollmentDate(enrollment.getEnrollmentDate())
                .student(student)
                .course(course)
                .build();
    }
}
