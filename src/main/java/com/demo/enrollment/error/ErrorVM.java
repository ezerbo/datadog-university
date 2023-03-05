package com.demo.enrollment.error;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class ErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
    
    private final String description;
    
    private List<FieldErrorVM> fieldErrors;
    
}