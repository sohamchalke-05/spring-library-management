package com.LibraryManagementHomework.LibraryManagement.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class ProdDataServiceImp implements DataService {

    @Override
    public String getData() {
        return "Prod Data";
    }
}
