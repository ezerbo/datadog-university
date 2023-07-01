package com.demo.tuition.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTuitionRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Double amount;

    private boolean paid;
}
