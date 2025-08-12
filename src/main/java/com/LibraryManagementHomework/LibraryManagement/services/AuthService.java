package com.LibraryManagementHomework.LibraryManagement.services;

import com.LibraryManagementHomework.LibraryManagement.DTO.loginDto;
import com.LibraryManagementHomework.LibraryManagement.DTO.loginResponseDto;
import com.LibraryManagementHomework.LibraryManagement.entities.SessionEntity;
import com.LibraryManagementHomework.LibraryManagement.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authentication;
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    public loginResponseDto login(loginDto loginDto){

        Authentication authentication1 = authentication.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        User user = (User) authentication1.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.generateNewSession(user,refreshToken);

        return new loginResponseDto(user.getId(),accessToken,refreshToken);
    }


    public loginResponseDto refreshToken(String oldRefreshToken) {
        Long userId = jwtService.getUserIdByToken(oldRefreshToken);

        if (!sessionService.isValid(oldRefreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired or invalid");
        }
        System.out.println("RefreshToken is Valid");
        sessionService.delete(oldRefreshToken);
        System.out.println("RefreshToken is Deleted");

        User user = userService.findById(userId);
        System.out.println("User is fetched successfully");
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        System.out.println("New Tokens Generating ");
        sessionService.generateNewSession(user, newRefreshToken);
        System.out.println("New Tokens Generated ");

        return new loginResponseDto(user.getId(), newAccessToken, newRefreshToken);
    }


    public void logout(User user){
        sessionService.logoutSession(user);
    }
}
