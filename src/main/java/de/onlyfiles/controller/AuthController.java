package de.onlyfiles.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.onlyfiles.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Auth")
@RestController
@RequestMapping("api/auth")
public class AuthController { // only for docu
    
    @Operation(summary = "Get session cookie and current user informations")
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response, @RequestParam MultiValueMap<String, String> paramMap) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
    @Operation(summary = "Instant logout")
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
