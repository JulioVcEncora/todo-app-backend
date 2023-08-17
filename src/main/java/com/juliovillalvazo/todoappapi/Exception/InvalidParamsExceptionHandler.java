package com.juliovillalvazo.todoappapi.Exception;

public class InvalidParamsExceptionHandler extends RuntimeException {
    public InvalidParamsExceptionHandler(String message) {
        super(message);
    }
}
