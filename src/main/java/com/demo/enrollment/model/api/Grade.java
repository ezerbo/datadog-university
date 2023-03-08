package com.demo.enrollment.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    private Long id;

    private Long studentId;

    private Long courseId;

    private String grade;

    private Date createDate;

    private Date updateDate;
}
