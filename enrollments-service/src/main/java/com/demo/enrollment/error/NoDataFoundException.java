package com.demo.enrollment.error;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(String message, Object... arguments) {
        super(String.format(message, arguments));
    }
}
