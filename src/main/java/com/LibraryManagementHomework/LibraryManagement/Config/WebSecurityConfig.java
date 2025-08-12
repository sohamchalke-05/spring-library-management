package com.LibraryManagementHomework.LibraryManagement.Config;

import com.LibraryManagementHomework.LibraryManagement.Filter.JwtFilter;
import com.LibraryManagementHomework.LibraryManagement.Handler.Oauth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static com.LibraryManagementHomework.LibraryManagement.entities.enums.Permissions.*;
import static com.LibraryManagementHomework.LibraryManagement.entities.enums.Role.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtFilter jwtFilter;
    private final Oauth2SuccessHandler oauth2SuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET,"/author/**","/book/**").hasAnyRole(USER.name(),ADMIN.name(), CREATOR.name())
                        .requestMatchers(HttpMethod.POST,"/author/**","/book/**").hasAnyRole(ADMIN.name(), CREATOR.name())
                .requestMatchers(HttpMethod.PUT,"/author/**","/book/**").hasAnyAuthority(POST_UPDATE.name(),USER_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE,"/author/**","/book/**").hasAnyAuthority(POST_DELETE.name(),USER_DELETE.name())
                .requestMatchers("/auth/signup","/auth/refresh","/auth/login","/home.html","/error").permitAll()
                .anyRequest().permitAll())

                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(authConfig ->
                        authConfig.failureUrl("/login?error=true")
                                .successHandler(oauth2SuccessHandler)
               );
        return httpSecurity.build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}