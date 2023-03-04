package com.demo.enrollment.service;

import com.demo.enrollment.error.DuplicateRecordException;
import com.demo.enrollment.error.NoDataFoundException;
import com.demo.enrollment.model.Enrollment;
import com.demo.enrollment.model.Student;
import com.demo.enrollment.model.api.CreateOrUpdateStudentRequest;
import com.demo.enrollment.model.api.GetStudentEnrollmentsResponse;
import com.demo.enrollment.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(CreateOrUpdateStudentRequest request) {
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
        return repository.save(student);
    }

    public Student update(Long id, CreateOrUpdateStudentRequest request) {
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
                .build();
        return repository.save(student);
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Optional<Student> getOne(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public GetStudentEnrollmentsResponse getEnrollments(Long id) {
        log.info("Getting enrollments for student with id: {}", id);
        return repository.findById(id)
                .map(student -> GetStudentEnrollmentsResponse.builder()
                        .student(student)
                        .courses(student.getEnrollments().stream().map(Enrollment::getCourse).collect(Collectors.toList()))
                        .build())
                .orElseThrow(() -> new NoDataFoundException("No student found with id %s", id));
    }
}
