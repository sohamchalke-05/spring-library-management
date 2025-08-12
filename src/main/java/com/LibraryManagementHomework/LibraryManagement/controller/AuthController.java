package com.LibraryManagementHomework.LibraryManagement.controller;

import com.LibraryManagementHomework.LibraryManagement.DTO.Signupdto;
import com.LibraryManagementHomework.LibraryManagement.DTO.UserDto;
import com.LibraryManagementHomework.LibraryManagement.DTO.loginDto;
import com.LibraryManagementHomework.LibraryManagement.DTO.loginResponseDto;
import com.LibraryManagementHomework.LibraryManagement.entities.User;
import com.LibraryManagementHomework.LibraryManagement.services.AuthService;
import com.LibraryManagementHomework.LibraryManagement.services.JwtService;
import com.LibraryManagementHomework.LibraryManagement.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping(path = "/signup")
    public ResponseEntity<UserDto> signup(@RequestBody Signupdto signupdto){
        UserDto userDto = userService.signup(signupdto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<loginResponseDto> login(@RequestBody loginDto loginDto, HttpServletResponse response){
        loginResponseDto loginResponseDto = authService.login(loginDto);
        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<loginResponseDto> refresh(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));
        loginResponseDto loginResponseDto = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping(path = "/logout")
    public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        final String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            throw new BadCredentialsException("Header is Invalid");
        }
        String token = header.split("Bearer ")[1];
        Long userId = jwtService.getUserIdByToken(token);
        User user = userService.findById(userId);

        authService.logout(user);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Logged out successfully");
    }
}
