package com.demo.enrollment.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCourseRequest {

    @Length(min = 3, max = 50, message = "Name should be between {min} & {max} characters longs")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull
    @FutureOrPresent(message = "The start date should be a future or present day")
    private Date startDate;

    @NotNull
    @Future(message = "The end date should be a future date")
    private Date endDate;
}
