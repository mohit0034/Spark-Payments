package com.example.spark.api.controller;

import com.example.spark.api.errorhandler.ErrorResponse;
import com.example.spark.api.errorhandler.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler (UserException ex){
        ErrorResponse error = new ErrorResponse();
        int code = ex.getError_code();
        error.setMessage(ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        switch (code)
        {
            case 400: status = HttpStatus.BAD_REQUEST;
                break;
            case 404: status = HttpStatus.NOT_FOUND;
                break;
            case 401: status = HttpStatus.UNAUTHORIZED;
                break;
            case 403: status = HttpStatus.FORBIDDEN;
                break;
            case 408: status = HttpStatus.REQUEST_TIMEOUT;
                break;

        }
        return new ResponseEntity<ErrorResponse>(error, status);
    }
}