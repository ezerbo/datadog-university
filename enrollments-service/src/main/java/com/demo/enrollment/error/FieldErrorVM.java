package com.demo.enrollment.error;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private String objectName;

    private String field;
    
    private String rejectedValue;
    
    private String message;

}