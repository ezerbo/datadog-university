package com.demo.enrollment.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {

    @Length(min = 3, max = 50, message = "Firstname should be between {min} & {max} characters long")
    @NotBlank(message = "The firstname is required")
    private String firstName;

    @Length(min = 3, max = 50, message = "Lastname should be between {min} & {max} characters long")
    @NotBlank(message = "The lastname is required")
    private String lastName;

    @NotBlank
    @Email(message = "'${validatedValue}' is not a valid email address")
    private String emailAddress;

    @NotNull
    @Past(message = "The date of birth should be in the past")
    private Date dob;

    @Length(min = 9, max = 9, message = "Lastname should be between {max} characters long")
    @NotBlank(message = "The ssn is required")
    private String ssn;
}
