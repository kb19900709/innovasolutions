package com.innova.controller.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private long epochTime;
    private String errMessage;
    private String servletPath;
}
