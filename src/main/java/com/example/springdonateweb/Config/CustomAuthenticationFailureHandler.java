package com.example.springdonateweb.Config;



import com.example.springdonateweb.Services.UsersService;
import com.example.springdonateweb.Services.interfaces.IUsersService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UsersService usersService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("username");
        if (!usersService.existsByEmail(email)) {
            response.sendRedirect("/login?error=email");
        } else {
            response.sendRedirect("/login?error=password");
        }
    }
}
