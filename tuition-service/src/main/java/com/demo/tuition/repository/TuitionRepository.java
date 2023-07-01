package com.demo.tuition.repository;

import com.demo.tuition.model.Tuition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TuitionRepository extends JpaRepository<Tuition, Long> {

    boolean existsByStudentId(Long studentId);

    Optional<Tuition> findByStudentId(Long studentId);
}
