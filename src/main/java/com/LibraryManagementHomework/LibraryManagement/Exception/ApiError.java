package com.LibraryManagementHomework.LibraryManagement.Exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
public class ApiError {
    private LocalDateTime localDateTime;
    private String error;
    private HttpStatus httpStatus;

    public ApiError(){
        this.localDateTime = LocalDateTime.now();
    }

    public ApiError(String error,HttpStatus httpStatus){
        this();
        this.error = error;
        this.httpStatus = httpStatus;
    }
}
