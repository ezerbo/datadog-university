package com.demo.enrollment.error;

public class ApiCallFailureException extends RuntimeException {

    public ApiCallFailureException(String message, Object... arguments) {
        super(String.format(message, arguments));
    }
}
