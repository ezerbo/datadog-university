package com.demo.enrollment.service;

import com.demo.enrollment.error.DuplicateRecordException;
import com.demo.enrollment.error.NoDataFoundException;
import com.demo.enrollment.model.Enrollment;
import com.demo.enrollment.model.EnrollmentId;
import com.demo.enrollment.model.Student;
import com.demo.enrollment.model.api.*;
import com.demo.enrollment.repository.CourseRepository;
import com.demo.enrollment.repository.EnrollmentRepository;
import com.demo.enrollment.repository.StudentRepository;
import com.demo.enrollment.service.client.GradesApiClient;
import com.demo.enrollment.service.client.TuitionApiClient;
import datadog.trace.api.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository repository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    private final TuitionApiClient tuitionApiClient;
    private final GradesApiClient gradesApiClient;

    public StudentService(StudentRepository repository,
                          EnrollmentRepository enrollmentRepository,
                          CourseRepository courseRepository,
                          TuitionApiClient tuitionApiClient,
                          GradesApiClient gradesApiClient) {
        this.repository = repository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.tuitionApiClient = tuitionApiClient;
        this.gradesApiClient = gradesApiClient;
    }

    @Trace(operationName = "student.create")
    public Student create(CreateStudentRequest request) {
        log.info("Creating a new student: {}", request);
        if (repository.existsBySsn(request.getSsn())) {
            log.error("A student with the same ssn already exists");
            throw new DuplicateRecordException("A student with the same ssn already exists");
        }

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailAddress(request.getEmailAddress())
                .ssn(request.getSsn())
                .dob(request.getDob())
                .build();
        student = repository.save(student);
        Tuition tuition = createTuitionRecord(student.getId(), request);
        student.setTuitionId(tuition.getId());
        return repository.save(student);
    }

    @Transactional
    @Trace(operationName = "student.update")
    public Student update(Long id, UpdateStudentRequest request) {
        log.info("Updating a student, request: {}, id: {}", request, id);
        Optional<Student> studentOp = repository.findById(id);
        if (!studentOp.isPresent()) {
            log.error("Attempting to update a missing student");
            throw new NoDataFoundException("No student found with id: %s", id);
        }

        if (!studentOp.get().getSsn().equals(request.getSsn()) && repository.existsBySsn(request.getSsn())) {
            log.error("A student with the same ssn already exists");
            throw new DuplicateRecordException("A student with the same ssn already exists");
        }
        Student student = Student.builder()
                .id(id)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailAddress(request.getEmailAddress())
                .ssn(request.getSsn())
                .dob(request.getDob())
                .enrollments(studentOp.get().getEnrollments())
                .tuitionId(studentOp.get().getTuitionId())
                .build();
        Tuition tuition = tuitionApiClient.get(student.getTuitionId());
        if (!Objects.equals(tuition.getAmount(), request.getTuitionAmount()) || tuition.isPaid() != request.isPaid()) {
            tuitionApiClient.update(tuition.getId(), SetTuitionRequest.builder()
                            .amount(request.getTuitionAmount())
                            .studentId(id)
                            .paid(request.isPaid())
                    .build());
        }
        return repository.save(student);
    }

    public List<Student> getAll() {
        log.info("Getting all students");
        return repository.findAll();
    }

    public Optional<Student> getOne(Long id) {
        log.info("Getting student with id: '{}'", id);
        return repository.findById(id);
    }

    public void delete(Long id) {
        log.info("Deleting student with id: '{}'", id);
        Student student = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("No student found with id: {}", id);
                    return new NoDataFoundException("No student found with id: %s", id);
                });
        tuitionApiClient.delete(student.getTuitionId());
        repository.delete(student);
    }

    @Trace(operationName = "student.getEnrollments")
    public GetStudentEnrollmentsResponse getEnrollments(Long id) {
        log.info("Getting enrollments for student with id: {}", id);
        return repository.findById(id)
                .map(student -> GetStudentEnrollmentsResponse.builder()
                        .student(student)
                        .enrollments(toEnrollments(student.getEnrollments()))
                        .build())
                .orElseThrow(() -> new NoDataFoundException("No student found with id %s", id));
    }

    @Transactional
    public void deleteEnrollment(Long studentId, Long courseId) {
        log.info("Deleting enrollments for student with id: {} and courseId: {}", studentId, courseId);
        if (!repository.existsById(studentId)) {
            log.error("No student found with id: {}", studentId);
            throw new NoDataFoundException("No student found with id: %s", studentId);
        }

        if (!courseRepository.existsById(courseId)) {
            log.error("No course found with id: {}", courseId);
            throw new NoDataFoundException("No course found with id: %s", courseId);
        }

        EnrollmentId id = EnrollmentId.builder()
                .studentId(studentId)
                .courseId(courseId)
                .build();
        Enrollment enrollment = enrollmentRepository.findById(id).get();
        enrollmentRepository.delete(enrollment);
        gradesApiClient.delete(enrollment.getGradeId());
    }

    public Tuition getTuitionDetails(Long id) {
        log.info("Getting tuition details for student with id: {}", id);
        return repository.findById(id)
                .map(student -> tuitionApiClient.get(student.getTuitionId()))
                .orElseThrow(() -> {
                    log.info("No student found with id: {}", id);
                    return new NoDataFoundException("No student found with id %s", id);
                });
    }

    public Tuition updateTuitionDetails(Long id, UpdateTuitionRequest request) {
        log.info("Updating tuition details for student with id: {}", id);
        return repository.findById(id)
                .map(student -> tuitionApiClient.update(student.getTuitionId(), SetTuitionRequest.builder()
                        .studentId(id)
                        .amount(request.getAmount())
                        .paid(request.isPaid())
                        .build()))
                .orElseThrow(() -> {
                    log.info("No student found with id: {}", id);
                    return new NoDataFoundException("No student found with id %s", id);
                });
    }

    private Tuition createTuitionRecord(Long studentId, CreateStudentRequest request) {
        CreateTuitionRequest createTuitionRequest = CreateTuitionRequest.builder()
                .studentId(studentId)
                .amount(request.getTuitionAmount())
                .paid(request.isPaid())
                .build();
        return tuitionApiClient.create(createTuitionRequest);
    }

    private List<CourseEnrollmentDTO> toEnrollments(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(e -> CourseEnrollmentDTO.builder()
                        .gradeId(e.getGradeId())
                        .enrollmentDate(e.getEnrollmentDate())
                        .course(e.getCourse())
                        .build())
                .collect(Collectors.toList());
    }
}
