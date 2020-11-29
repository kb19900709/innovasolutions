package com.innova.service.constant;

import lombok.Getter;

@Getter
public enum ValidationErrorEnum {
    PASSWORD_INVALID_LENGTH("the password must be between 5 and 12 characters in length"),
    PASSWORD_INVALID_CHARACTER("the password consists of a mixture of lowercase letters and numerical digits only, with at least one of each"),
    PASSWORD_INVALID_SEQUENCE("the password must not contain any sequence of characters immediately followed by the same sequence.");

    ValidationErrorEnum(String message) {
        this.message = message;
    }

    private String message;
}
