package de.onlyfiles.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import de.onlyfiles.Permission;

@Configuration
@EnableMethodSecurity(prePostEnabled=true)
public class MethodSecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
      RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
      roleHierarchyImpl.setHierarchy(Arrays.toString(Permission.values()).replaceFirst("^.", "ROLE_").replaceFirst(".$", "").replaceAll(", "," > ROLE_"));
      return roleHierarchyImpl;
    }
    
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy());
        return handler;
    }
}
