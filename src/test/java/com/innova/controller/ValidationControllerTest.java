package com.innova.controller;

import com.innova.controller.bean.ErrorResponse;
import com.innova.controller.bean.PasswordRequest;
import com.innova.controller.bean.PasswordResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void validatePasswordOkTest() {
        Stream.of("aaa123", "abcab123", "1abc2abc", "abcab122123", "bbbabbbc123")
                .map(this::getPasswordRequest)
                .map(this::getPasswordResponse)
                .map(PasswordResponse::getMessage)
                .forEach(passwordResponseMessage ->
                        assertThat(passwordResponseMessage)
                                .as("integration test: password test, OK", passwordResponseMessage)
                                .isEqualTo("OK")
                );
    }

    @Test
    public void validatePasswordFailedTest() {
        Stream.of(
                "abab123", "abcab122122", "345aabaab678", "345baabaaa678", "aabbaabb123",
                "aaaaaa123", "aaaa123", "123aaaaaa",  "123aaaa", "",
                "ab12", "ab12bc12cc12d", "abcde", "12345", "11abcabc11"
        )
        .map(this::getPasswordRequest)
        .map(this::getErrorResponse)
        .map(ErrorResponse::getErrMessage)
        .forEach(errorMessage ->
            assertThat(errorMessage)
                    .as("integration test: password test, failed", errorMessage)
                    .isNotBlank()
        );
    }

    private PasswordResponse getPasswordResponse(PasswordRequest passwordRequest) {
        return restTemplate.postForObject(getValidatePasswordUrl(), passwordRequest, PasswordResponse.class);
    }

    private ErrorResponse getErrorResponse(PasswordRequest passwordRequest) {
        return restTemplate.postForObject(getValidatePasswordUrl(), passwordRequest, ErrorResponse.class);
    }

    private String getValidatePasswordUrl() {
        return String.format("http://localhost:%s/validation/password", port);
    }

    private PasswordRequest getPasswordRequest(String password) {
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setPassword(password);
        return passwordRequest;
    }
}
