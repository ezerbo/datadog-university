package com.demo.enrollment.model.api;

import com.demo.enrollment.model.Course;
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
public class CourseEnrollmentDTO {

    private Date enrollmentDate;

    private Long gradeId;

    private Course course;
}
