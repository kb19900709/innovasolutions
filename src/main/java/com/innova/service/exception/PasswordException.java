package com.innova.service.exception;

import com.innova.service.constant.ValidationErrorEnum;

public class PasswordException extends ValidationException {
    public PasswordException(ValidationErrorEnum validationErrorEnum) {
        super(validationErrorEnum);
    }
}
