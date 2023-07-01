package com.demo.grade.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGradeRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

}
