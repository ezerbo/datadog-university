package com.demo.enrollment.model.api;

import com.demo.enrollment.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrollmentDTO {

    private Date enrollmentDate;

    private Long gradeId;

    private Student student;
}
