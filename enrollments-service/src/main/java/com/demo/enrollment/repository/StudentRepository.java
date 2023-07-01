package com.demo.enrollment.repository;

import com.demo.enrollment.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsBySsn(String ssn);
}
