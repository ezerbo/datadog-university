package com.demo.enrollment.error;

public class InvalidCourseDatesException extends RuntimeException {

    public InvalidCourseDatesException(String message, Object... arguments) {
        super(String.format(message, arguments));
    }
}
