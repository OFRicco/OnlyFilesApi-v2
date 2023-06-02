package de.onlyfiles.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    @Bean
    private SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
}
