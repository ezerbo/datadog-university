package com.demo.grade.error;

public class NoGradeFoundException extends RuntimeException {

    public NoGradeFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
