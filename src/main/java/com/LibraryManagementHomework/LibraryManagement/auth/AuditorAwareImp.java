package com.LibraryManagementHomework.LibraryManagement.auth;

import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImp implements AuditorAware<String> {

    @Override
    public Optional getCurrentAuditor() {
        return Optional.of("Soham Ganesh Chalke");
    }
}

