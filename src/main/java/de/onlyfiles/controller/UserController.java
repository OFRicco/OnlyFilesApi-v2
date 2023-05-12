package de.onlyfiles.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RestController;

import de.onlyfiles.exception.DeleteFailedException;
import de.onlyfiles.exception.NoCurrentPrincipalException;
import de.onlyfiles.exception.ObjectAlreadyExistsException;
import de.onlyfiles.exception.UserNotFoundException;
import de.onlyfiles.model.File;
import de.onlyfiles.model.Group;
import de.onlyfiles.model.User;
import de.onlyfiles.repository.UserRepository;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired 
    PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        if(userRepository.existsByName(user.getName())) {
            throw new ObjectAlreadyExistsException();
        }
        
        User newUser = userRepository.save(user);
        
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
    
    @GetMapping(path = {"", "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(Principal principal, @PathVariable(value="id", required = false) Optional<Long> id) {
        
        User user = null;
        if(id.isPresent()) {
            user = userRepository.findUserById(id.get());
        } else {
            if(principal == null) {
                throw new NoCurrentPrincipalException();
            }
            
            user = userRepository.findByName(principal.getName());
        }
        
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(path = {"", "/{id}"})
    public ResponseEntity<?> deleteUser(Principal principal, @PathVariable(value="id", required = false) Optional<Long> id) {
        boolean success = false;
        
        if(id.isPresent()) {
            success = userRepository.deleteUserById(id.get());
        } else {
            if(principal == null) {
                throw new NoCurrentPrincipalException();
            }
            success = userRepository.deleteByName(principal.getName());
        }
        
        if(success) {
            throw new DeleteFailedException();
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"/groups", "/groups/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getGroups(Principal principal, @PathVariable(value="id", required = false) Optional<Long> id) {
        
        User user = null;
        if(id.isPresent()) {
            user = userRepository.findUserById(id.get());
        } else {
            if(principal == null) {
                throw new NoCurrentPrincipalException();
            }
            user = userRepository.findByName(principal.getName());
        }
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<Group> groups = user.getGroups();
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
    
    @GetMapping(path = {"/owned/groups/","/owned/groups/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getOwnedGroups(Principal principal, @PathVariable(value="name", required = false) Optional<Long> id) {
        User user = null;
        
        if(id.isPresent()) {
            user = userRepository.findUserById(id.get());
        } else {
            if(principal == null) {
                throw new NoCurrentPrincipalException();
            }
            user = userRepository.findByName(principal.getName());
        }
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<Group> groups = user.getOwnedGroups();
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
    
    @GetMapping(path = {"/owned/files", "/owned/files/{id}"} , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<File>> getOwnedFiles(Principal principal, @PathVariable(value="name", required = false) Optional<Long> id) {
        User user = null;
        
        if(id.isPresent()) {
            user = userRepository.findUserById(id.get());
        } else {
            if(principal == null) {
                throw new NoCurrentPrincipalException();
            }
            
            user = userRepository.findByName(principal.getName());
        }
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<File> files = user.getOwnedFiles();
        
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping(path = {"/files","/files/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<File>> getFiles(Principal principal, @PathVariable(value="id", required = false) Optional<Long> id) {
        User user = null;
        
        if(id.isPresent()) {
            user = userRepository.findUserById(id.get());
        } else {
            if(principal == null) {
                throw new NoCurrentPrincipalException();
            }
            
            user = userRepository.findByName(principal.getName());
        }
        
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<File> files = new HashSet<>();
        
        for(Group group : user.getGroups()) {
            for(File file : group.getFiles()) {
                files.add(file);
            }
        }
        
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}
