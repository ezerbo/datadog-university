package com.demo.grade.service;

import com.demo.grade.error.NoGradeFoundException;
import com.demo.grade.error.UnpaidTuitionException;
import com.demo.grade.model.Grade;
import com.demo.grade.model.api.CreateGradeRequest;
import com.demo.grade.model.api.UpdateGradeRequest;
import com.demo.grade.repository.GradeRepository;
import com.demo.grade.service.client.TuitionApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GradeService {

    private final GradeRepository repository;
    private final EnrollmentsApiClient enrollmentsApiClient;
    private final TuitionApiClient tuitionApiClient;

    public GradeService(GradeRepository repository,
                        EnrollmentsApiClient enrollmentsApiClient,
                        TuitionApiClient tuitionApiClient) {
        this.repository = repository;
        this.enrollmentsApiClient = enrollmentsApiClient;
        this.tuitionApiClient = tuitionApiClient;
    }

    public Grade create(CreateGradeRequest request) {
        log.info("Creating a new grade entry: {}", request);
        if (!(enrollmentsApiClient.isValidStudent(request.getStudentId())
                || enrollmentsApiClient.isValidCourse(request.getCourseId()))) {
            log.error("Unable to find student or course");
            throw new RuntimeException("Invalid student or course Id");
        }

        if (repository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())) {
            log.error("A grade already exists for the student & course: {}", request);
            throw new RuntimeException("A grade already exists for the student & course");
        }
        Grade grade = Grade.builder()
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .build();
        return repository.save(grade);
    }

    public Grade update(Long id, UpdateGradeRequest request) {
        log.info("Updating a grade, gradeId: {}, request: {}", id, request);
        Grade grade = repository.findById(id)
                .map(g -> {
                    g.setGrade(request.getGrade());
                    return g;
                }).orElseThrow(() -> new RuntimeException(String.format("No grades found with id '%s'", id)));
        return repository.save(grade);
    }

    public Grade get(Long id, Long tuitionId) {
        log.info("Getting grade with id: '{}' & tuitionId: {}", id, tuitionId);
        Grade grade = repository.findById(id)
                .orElseThrow(() -> new NoGradeFoundException("No grade found with id: '%s'", id));
        if (!tuitionApiClient.isTuitionPaid(tuitionId)) {
            log.info("Student has not paid tuition, grade cannot be released");
            throw new UnpaidTuitionException("Student with id: %s has not paid tuition, grades cannot be released",
                    grade.getStudentId());
        }
        return grade;
    }

    public void delete(Long id) {
        Grade grade = repository.findById(id)
                .orElseThrow(() -> new NoGradeFoundException("No grade found with id: '%s'", id));
        repository.delete(grade);
    }
}
