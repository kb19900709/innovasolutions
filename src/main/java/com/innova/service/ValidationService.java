package com.innova.service;

import com.innova.service.exception.ValidationException;

public interface ValidationService<T> {
    void validate(T t) throws ValidationException;
}
