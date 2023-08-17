package com.juliovillalvazo.todoappapi.Exception;


import com.juliovillalvazo.todoappapi.Misc.CustomErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(InvalidParamsExceptionHandler.class)
    public ResponseEntity<CustomErrorMessage> handleBadRequest(InvalidParamsExceptionHandler e, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundExceptionHandler.class)
    public ResponseEntity<CustomErrorMessage> handleNotFound(NotFoundExceptionHandler e, WebRequest request) {
        CustomErrorMessage body = new CustomErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}
