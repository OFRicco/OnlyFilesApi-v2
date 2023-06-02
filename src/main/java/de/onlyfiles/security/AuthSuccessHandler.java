package de.onlyfiles.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.onlyfiles.model.User;
import de.onlyfiles.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ObjectMapper mapper;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null) {
            response.setHeader("content-type", "application/json");

            User user = userRepository.findByName(authentication.getName());
            response.getWriter().write(mapper.writeValueAsString(user));
        }
    }
}
