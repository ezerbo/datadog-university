package com.demo.enrollment.repository;

import com.demo.enrollment.model.Enrollment;
import com.demo.enrollment.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

}
