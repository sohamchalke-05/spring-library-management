package com.LibraryManagementHomework.LibraryManagement.Handler;

import com.LibraryManagementHomework.LibraryManagement.entities.User;
import com.LibraryManagementHomework.LibraryManagement.services.JwtService;
import com.LibraryManagementHomework.LibraryManagement.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

import static com.LibraryManagementHomework.LibraryManagement.entities.enums.Role.USER;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    private final JwtService jwtService;

    @Value("${deploy.env}")
    private String deployEnv;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info(email);

        User user1 = userService.findByEmail(email);
        if (user1 == null) {
            User newuser = User.builder()
                    .email(email)
                    .name(name)
                    .roles(oAuth2User.getAttribute("roles"))
                    .build();
            user1 = userService.save(newuser);
        }

        String accessToken = jwtService.generateAccessToken(user1);
        String refreshToken = jwtService.generateRefreshToken(user1);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        String frontEndUrl = "http://localhost:8080/home.html?token="+accessToken;

        response.sendRedirect(frontEndUrl);
    }
}
