package com.juliovillalvazo.todoappapi;

public class NotFoundExceptionHandler extends RuntimeException{
    public NotFoundExceptionHandler(String message) {
            super(message);
        }
}
