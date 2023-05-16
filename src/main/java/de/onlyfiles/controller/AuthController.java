package de.onlyfiles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.onlyfiles.model.User;
import de.onlyfiles.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Tag(name = "Auth")
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    AuthenticationProvider authenticationManager;
    
    @Autowired
    UserRepository userRepository;
    
    @Operation(summary = "Get session cookie and current user informations")
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<User> login(HttpServletRequest request, @RequestParam MultiValueMap<String, String> paramMap) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(!(securityContext.getAuthentication() instanceof UsernamePasswordAuthenticationToken)) {
            
            UsernamePasswordAuthenticationToken authReqeuest= new UsernamePasswordAuthenticationToken((String)paramMap.get("username").get(0), (String)paramMap.get("password").get(0));
            Authentication auth = authenticationManager.authenticate(authReqeuest);
            
            securityContext.setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        }
        
        User user = userRepository.findByName((String)paramMap.get("username").get(0));
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @Operation(summary = "Instant logout")
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){      
           new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
        
    }
    
}
