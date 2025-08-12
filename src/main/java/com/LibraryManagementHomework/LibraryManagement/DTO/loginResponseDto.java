package com.LibraryManagementHomework.LibraryManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class loginResponseDto {
    private Long id;
    private String accessToken;
    private String refreshToken;
}
