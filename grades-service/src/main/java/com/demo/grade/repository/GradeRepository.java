package com.demo.grade.repository;

import com.demo.grade.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
