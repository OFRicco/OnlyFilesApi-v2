package de.onlyfiles.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.onlyfiles.exception.NoCurrentPrincipalException;
import de.onlyfiles.exception.UserAlreadyExistsException;
import de.onlyfiles.model.User;
import de.onlyfiles.repository.UserRepository;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired 
    PasswordEncoder passwordEncoder;
    
    @GetMapping(produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> getUser(Principal principal) {
        if(principal != null) {
            
            User user = userRepository.findUserByName(principal.getName());
            user.setPassword(null);
            
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        
        throw new NoCurrentPrincipalException();
    }
    
    @GetMapping(path = "/{name}", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> getSpecificUser(@PathVariable(value="name") String name) {
        if(name != null) {
            
            User user = userRepository.findUserByName(name);
            user.setPassword(null);
            
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        
        throw new NoCurrentPrincipalException();
    }
    
    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody User user, @RequestParam(value = "encrypted") boolean encrypted) {
        
        if(!userRepository.existsByName(user.getName())) {
            if(!encrypted) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            User createdUser = userRepository.save(user);
            return new ResponseEntity<>(createdUser.getId(), HttpStatus.OK);
        }
        throw new UserAlreadyExistsException();
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity<?> deleteUser(@PathVariable(value="name") String name) {
        if(name != null) {
            userRepository.delete(userRepository.findUserByName(name));
            
            return new ResponseEntity<>(HttpStatus.OK);
        }
        
        throw new NoCurrentPrincipalException();
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(Principal principal) {
        if(principal != null) {

            boolean success = userRepository.deleteByName(principal.getName());
            
            return new ResponseEntity<>(success, HttpStatus.OK);
        }
        
        throw new NoCurrentPrincipalException();
    }
    
    @DeleteMapping(path = "/{name}")
    public ResponseEntity<Boolean> deleteSpecificUser(@PathVariable(value="name") String name) {
        if(name != null) {

            boolean success = userRepository.deleteByName(name);
            
            return new ResponseEntity<>(success, HttpStatus.OK);
        }
        
        throw new NoCurrentPrincipalException();
    }
}
