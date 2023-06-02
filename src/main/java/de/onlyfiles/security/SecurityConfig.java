package de.onlyfiles.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
      return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new MySQLUserDetailsService();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/api/user").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll().defaultSuccessUrl("/", false).loginProcessingUrl("/api/auth/login").successHandler(authSuccessHandler)
            .and()
            .logout().logoutUrl("/api/auth/logout").permitAll();
        
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry).and().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        
        return http.build();
    }
    
    @Bean 
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }

}
