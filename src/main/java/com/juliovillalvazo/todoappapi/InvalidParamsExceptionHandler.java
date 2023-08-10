package com.juliovillalvazo.todoappapi;

public class InvalidParamsExceptionHandler extends RuntimeException {
    public InvalidParamsExceptionHandler(String message) {
        super(message);
    }
}
