package com.innova.controller;

import com.innova.controller.bean.ErrorResponse;
import com.innova.service.exception.PasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(value = PasswordException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse passwordExceptionHandler(PasswordException passwordException, WebRequest webRequest) {
        return ErrorResponse.builder()
                .servletPath(((ServletWebRequest) webRequest).getRequest().getServletPath())
                .epochTime(Instant.now().toEpochMilli())
                .errMessage(passwordException.getMessage())
                .build();
    }
}
