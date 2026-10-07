package com.harshpatil.com.springSecurity.advice;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrors {

    private LocalDateTime timeStamp;
    private String error;
    private HttpStatus statusCode;

    public ApiErrors(String error,HttpStatus statusCode)
    {
        this();
        this.error=error;
        this.statusCode=statusCode;
    }

    public ApiErrors()
    {
        this.timeStamp=LocalDateTime.now();
    }
}
