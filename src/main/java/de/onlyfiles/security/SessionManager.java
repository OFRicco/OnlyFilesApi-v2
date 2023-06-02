package de.onlyfiles.security;

import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    private Deque<Long> unupdatedSessions = new ArrayDeque<>();

    @Bean
    private SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    public Deque<Long> getUnupdatedSessions() {
        return unupdatedSessions;
    }
    
    public void setUnupdatedSessions(Deque<Long> unupdatedSessions) {
        this.unupdatedSessions = unupdatedSessions;
    }
    
}
