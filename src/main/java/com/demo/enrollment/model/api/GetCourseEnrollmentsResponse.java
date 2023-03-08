package com.demo.enrollment.model.api;

import com.demo.enrollment.model.Course;
import com.demo.enrollment.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCourseEnrollmentsResponse {

    private Course course;

    private List<StudentEnrollmentDTO> enrollments;
}
