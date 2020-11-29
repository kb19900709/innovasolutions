package com.innova.controller;

import com.innova.controller.bean.PasswordRequest;
import com.innova.controller.bean.PasswordResponse;
import com.innova.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("validation")
public class ValidationController {

    private ValidationService passwordValidationService;

    @Autowired
    public ValidationController(
            @Qualifier("passwordValidationService") ValidationService passwordValidationService) {
        this.passwordValidationService = passwordValidationService;
    }

    @PostMapping("password")
    public PasswordResponse validatePassword(@RequestBody PasswordRequest passwordRequest) {
        String defaultSuccessfulMessage = "OK";

        passwordValidationService.validate(passwordRequest.getPassword());

        // some other messages might be added after above validation, like change password message

        PasswordResponse response = new PasswordResponse();
        response.setMessage(defaultSuccessfulMessage);
        return response;
    }

}
