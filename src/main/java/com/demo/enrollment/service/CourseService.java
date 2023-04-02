package com.demo.enrollment.service;

import com.demo.enrollment.error.DuplicateRecordException;
import com.demo.enrollment.error.InvalidCourseDatesException;
import com.demo.enrollment.error.NoDataFoundException;
import com.demo.enrollment.model.Enrollment;
import com.demo.enrollment.model.EnrollmentId;
import com.demo.enrollment.model.Course;
import com.demo.enrollment.model.Student;
import com.demo.enrollment.model.api.*;
import com.demo.enrollment.repository.EnrollmentRepository;
import com.demo.enrollment.repository.CourseRepository;
import com.demo.enrollment.repository.StudentRepository;
import com.demo.enrollment.service.client.GradesApiClient;
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
    private final GradesApiClient gradesApiClient;

    public CourseService(CourseRepository repository,
                         StudentRepository studentRepository,
                         EnrollmentRepository enrollmentRepository,
                         GradesApiClient gradesApiClient) {
        this.repository = repository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.gradesApiClient = gradesApiClient;
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

    public List<Course> getAll(Long studentId) {
        List<Course> courses = repository.findAll();
        if (studentId != null) {
            log.info("Getting courses for enrollment, studentId: {}", studentId);
            return courses.stream()
                    .filter(course -> course.getEnrollments().stream()
                            .noneMatch(enrollment -> enrollment.getStudent().getId().equals(studentId)))
                    .collect(Collectors.toList());
        }
        log.info("Getting all courses");
        return courses;
    }

    public Optional<Course> getOne(Long id) {
        log.info("Getting course with id: '{}'", id);
        return repository.findById(id);
    }

    public void delete(Long id) {
        log.info("Deleting course with id: '{}'", id);
        repository.deleteById(id);
    }

    @Trace(operationName = "course.getEnrollments")
    public GetCourseEnrollmentsResponse getEnrollments(Long id) {
        log.info("Getting enrollments for course with id: {}", id);
        return repository.findById(id)
                .map(course -> GetCourseEnrollmentsResponse.builder()
                        .course(course)
                        .enrollments(toEnrollments(course.getEnrollments()))
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
        Grade grade = createGrade(student, course);
        Enrollment enrollment = Enrollment.builder()
                .id(buildEnrollmentId(request.getStudentId(), courseId))
                .enrollmentDate(new Date())
                .course(course)
                .student(student)
                .gradeId(grade.getId())
                .build();
        log.info("Saving course enrollment: {}", enrollment);
        enrollmentRepository.save(enrollment);
        return CourseEnrollmentResponse.builder()
                .enrollmentDate(enrollment.getEnrollmentDate())
                .student(student)
                .gradeId(grade.getId())
                .course(course)
                .build();
    }

    public GradeDTO getGrade(Long id, Long studentId) {
        Enrollment enrollment = enrollmentRepository.findById(buildEnrollmentId(studentId, id))
                .orElseThrow(() -> new NoDataFoundException("No enrollment found with studentId '%s' and courseId: '%s'",
                        studentId, id));
        Grade grade = gradesApiClient.get(enrollment);
        log.info("Retrieved grade: {}", grade);
        return GradeDTO.builder()
                .grade(grade.getGrade())
                .course(enrollment.getCourse())
                .student(enrollment.getStudent())
                .build();
    }

    public GradeDTO setGrade(Long id, SetGradeRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(buildEnrollmentId(request.getStudentId(), id))
                .orElseThrow(() -> new NoDataFoundException("No enrollment found with studentId '%s' and courseId: '%s'",
                        request.getStudentId(), id));
        UpdateGradeRequest updateGradeRequest = UpdateGradeRequest.builder()
                .courseId(id)
                .studentId(request.getStudentId())
                .grade(request.getGrade())
                .build();
        Grade grade = gradesApiClient.update(enrollment.getGradeId(), updateGradeRequest);
        return GradeDTO.builder()
                .grade(grade.getGrade())
                .student(enrollment.getStudent())
                .course(enrollment.getCourse())
                .build();
    }

    private Grade createGrade(Student student, Course course) {
        CreateGradeRequest request = CreateGradeRequest.builder()
                .studentId(student.getId())
                .courseId(course.getId())
                .build();
        return gradesApiClient.create(request);
    }

    private List<StudentEnrollmentDTO> toEnrollments(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(e -> StudentEnrollmentDTO.builder()
                        .gradeId(e.getGradeId())
                        .enrollmentDate(e.getEnrollmentDate())
                        .student(e.getStudent())
                        .build())
                .collect(Collectors.toList());
    }

    private EnrollmentId buildEnrollmentId(Long studentId, Long courseId) {
        return EnrollmentId.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();
    }
}
