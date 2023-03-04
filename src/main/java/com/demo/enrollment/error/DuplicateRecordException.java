package com.demo.enrollment.error;

public class DuplicateRecordException extends RuntimeException {

    public DuplicateRecordException(String message, Object... arguments) {
        super(String.format(message, arguments));
    }
}
