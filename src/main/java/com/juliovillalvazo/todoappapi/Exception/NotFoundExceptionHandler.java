package com.juliovillalvazo.todoappapi.Exception;

public class NotFoundExceptionHandler extends RuntimeException{
    public NotFoundExceptionHandler(String message) {
            super(message);
        }
}
