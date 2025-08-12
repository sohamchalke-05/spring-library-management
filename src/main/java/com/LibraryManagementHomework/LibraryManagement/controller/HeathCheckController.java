package com.LibraryManagementHomework.LibraryManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathCheckController {

    @GetMapping("/")
    public ResponseEntity<String> heathCheckController(){
        return ResponseEntity.ok("OK");
    }
}
