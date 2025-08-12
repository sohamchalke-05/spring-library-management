package com.LibraryManagementHomework.LibraryManagement.Filter;

import com.LibraryManagementHomework.LibraryManagement.entities.User;
import com.LibraryManagementHomework.LibraryManagement.services.JwtService;
import com.LibraryManagementHomework.LibraryManagement.services.SessionService;
import com.LibraryManagementHomework.LibraryManagement.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            System.out.println("JwtFilter triggered!");
            final String header = request.getHeader("Authorization");
            if(header == null || !header.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }

            String token = header.split("Bearer ")[1];
            Long userId = jwtService.getUserIdByToken(token);
            System.out.println("This is userid: " + userId);

            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User user = userService.findById(userId);
                if(user == null){
                    System.out.println("User not found  " + userId);
                    return;
                }

                System.out.println("User found  " + user);

                UsernamePasswordAuthenticationToken passwordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                passwordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
                System.out.println("SecurityContextHolder set with user: " + user.getEmail());
            }
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request,response,null,e);
        }
    }
}
