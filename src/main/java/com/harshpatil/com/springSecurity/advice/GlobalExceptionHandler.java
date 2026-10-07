package com.harshpatil.com.springSecurity.advice;


import com.harshpatil.com.springSecurity.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrors> handleResourceNotFoundException(ResourceNotFoundException exception)
    {
        ApiErrors apiErrors=new ApiErrors(exception.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(apiErrors,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrors> handleAuthenticationException(AuthenticationException exception)
    {
        ApiErrors apiErrors=new ApiErrors(exception.getLocalizedMessage(),HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<ApiErrors>(apiErrors,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrors> handleJwtException(JwtException exception)
    {
        ApiErrors apiErrors=new ApiErrors( exception.getLocalizedMessage() ,HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiErrors,HttpStatus.UNAUTHORIZED);
    }
}
