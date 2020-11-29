package com.innova.service.exception;


import com.innova.service.constant.ValidationErrorEnum;

public abstract class ValidationException extends RuntimeException {
    public ValidationException(ValidationErrorEnum validationErrorEnum) {
        super(validationErrorEnum.getMessage());
    }
}
