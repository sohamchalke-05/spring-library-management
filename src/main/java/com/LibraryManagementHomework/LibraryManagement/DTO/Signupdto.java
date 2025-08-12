package com.LibraryManagementHomework.LibraryManagement.DTO;

import com.LibraryManagementHomework.LibraryManagement.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class Signupdto {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    private String SessionSub;
}
