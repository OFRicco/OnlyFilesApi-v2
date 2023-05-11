package de.onlyfiles.controller;

import java.security.Principal;
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
    public ResponseEntity<Long> createUser(@RequestBody User user) {
        if(userRepository.existsByName(user.getName())) {
            throw new ObjectAlreadyExistsException();
        }
        
        User createdUser = userRepository.save(user);
        
        return new ResponseEntity<>(createdUser.getId(), HttpStatus.OK);
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(Principal principal) {
        if(principal == null) {
            throw new NoCurrentPrincipalException();
        }

        User user = userRepository.findByName(principal.getName());
        
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @GetMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable(value="name", required = true) String name) {
        User user = userRepository.findByName(name);
        
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        user.setPassword(null);
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(Principal principal) {
        if(principal == null) {
            throw new NoCurrentPrincipalException();
        }
        
        boolean success = userRepository.deleteByName(principal.getName());
        
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable(value="name", required = true) String name) {
        boolean success = userRepository.deleteByName(name);
        
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @GetMapping(path = "/groups" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getGroups(Principal principal) {
        if(principal == null) {
            throw new NoCurrentPrincipalException();
        }
        
        User user = userRepository.findByName(principal.getName());
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<Group> groups = user.getGroups();
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(path = "/{name}/groups" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getGroups(@PathVariable(value="name", required = true) String name) {
        User user = userRepository.findByName(name);
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<Group> groups = user.getGroups();
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(path = "/owned/groups" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getOwnedGroups(Principal principal) {
        if(principal == null) {
            throw new NoCurrentPrincipalException();
        }
        
        User user = userRepository.findByName(principal.getName());
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<Group> groups = user.getOwnedGroups();
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(path = "/{name}/owned/groups" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getOwnedGroups(@PathVariable(value="name", required = true) String name) {
        User user = userRepository.findByName(name);
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<Group> groups = user.getOwnedGroups();
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
    
    @GetMapping(path = "/owned/files" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<File>> getOwnedFiles(Principal principal) {
        if(principal == null) {
            throw new NoCurrentPrincipalException();
        }
        
        User user = userRepository.findByName(principal.getName());
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<File> files = user.getOwnedFiles();
        
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping(path = "/{name}/owned/files" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<File>> getOwnedFiles(@PathVariable(value="name", required = true) String name) {
        User user = userRepository.findByName(name);
            
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        Set<File> files = user.getOwnedFiles();
        
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    
}
