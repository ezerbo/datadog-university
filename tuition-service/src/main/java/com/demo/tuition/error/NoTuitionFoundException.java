package com.demo.tuition.error;

public class NoTuitionFoundException extends RuntimeException {

    public NoTuitionFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
