package de.onlyfiles.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import de.onlyfiles.model.User;
import de.onlyfiles.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class KeepUserUpdatedInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
        SecurityContext securityContext = SecurityContextHolder.getContext();
        MySQLUser principal = (MySQLUser) securityContext.getAuthentication().getPrincipal();

        for (Long id : sessionManager.getUnupdatedSessions()) {
            if (!sessionRegistry.getAllSessions(principal, false).isEmpty()) {
                if (id == principal.getId()) {
        /*
                    request.logout();
                    sessionManager.getUnupdatedSessions().remove(id);
                }
            } else if (!sessionRegistry.getAllSessions(principal, true).isEmpty()) {
                sessionManager.getUnupdatedSessions().remove(id);
            }
        }
        */

        return true;
    }

}
