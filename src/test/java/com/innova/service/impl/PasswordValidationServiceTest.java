package com.innova.service.impl;

import com.innova.service.constant.ValidationErrorEnum;
import com.innova.service.exception.PasswordException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class PasswordValidationServiceTest {

    private PasswordValidationService passwordValidationService = new PasswordValidationService();

    @Test
    public void validateOkTest() {
        Arrays.asList("aaa123", "abcab123", "1abc2abc", "abcab122123", "bbbabbbc123")
                .forEach(passwordValidationService::validate);
    }

    @Test
    public void validLengthEmptyTest() {
        assertThatThrownBy(() -> passwordValidationService.validateLength(""))
                .as("the password is an empty string")
                .isInstanceOf(PasswordException.class)
                .hasMessage(ValidationErrorEnum.PASSWORD_INVALID_LENGTH.getMessage());
    }

    @Test
    public void validLengthInsufficientLengthTest() {
        assertThatThrownBy(() -> passwordValidationService.validateLength("ab12"))
                .as("the length of the password is not enough")
                .isInstanceOf(PasswordException.class)
                .hasMessage(ValidationErrorEnum.PASSWORD_INVALID_LENGTH.getMessage());
    }

    @Test
    public void validLengthOverLengthTest() {
        assertThatThrownBy(() -> passwordValidationService.validateLength("ab12bc12cc12d"))
                .as("the password is over length")
                .isInstanceOf(PasswordException.class)
                .hasMessage(ValidationErrorEnum.PASSWORD_INVALID_LENGTH.getMessage());
    }

    @Test
    public void validCharacterWithoutNumbersTest() {
        assertThatThrownBy(() -> passwordValidationService.validateCharacter("abcde"))
                .as("the password doesn't contain any numbers")
                .isInstanceOf(PasswordException.class)
                .hasMessage(ValidationErrorEnum.PASSWORD_INVALID_CHARACTER.getMessage());
    }

    @Test
    public void validCharacterWithoutLettersTest() {
        assertThatThrownBy(() -> passwordValidationService.validateCharacter("12345"))
                .as("the password doesn't contain any letters")
                .isInstanceOf(PasswordException.class)
                .hasMessage(ValidationErrorEnum.PASSWORD_INVALID_CHARACTER.getMessage());
    }

    @Test
    public void validSequenceFailedTest() {
        Arrays.asList(
                "abab123", "abcab122122", "345aabaab678", "345baabaaa678", "aabbaabb123", "aaaaaa123", "aaaa123", "123aaaaaa", "123aaaa"
        ).forEach(failedPassword ->
                assertThatThrownBy(() -> passwordValidationService.validateSequence(failedPassword))
                        .as("char sequence test", failedPassword)
                        .isInstanceOf(PasswordException.class)
                        .hasMessage(ValidationErrorEnum.PASSWORD_INVALID_SEQUENCE.getMessage())
        );
    }
}
