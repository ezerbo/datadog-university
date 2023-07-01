package com.demo.grade.error;

public class UnpaidTuitionException extends RuntimeException {

    public UnpaidTuitionException(String message, Object... args) {
        super(String.format(message, args));
    }
}
