package com.LibraryManagementHomework.LibraryManagement.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevDataServiceImp implements DataService{
    @Override
    public String getData() {
        return "Dev Data";
    }
}
