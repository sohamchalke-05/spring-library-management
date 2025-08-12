package com.LibraryManagementHomework.LibraryManagement.Exception;

import lombok.Data;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@Data
@RestControllerAdvice
public class GlobalHealthException {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> AuthenticationException(AuthenticationException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> JwtException(JwtException ex){
        ApiError apiError  = new ApiError(ex.getLocalizedMessage(),HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

}
