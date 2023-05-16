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
import de.onlyfiles.exception.UserNotFoundException;
import de.onlyfiles.model.File;
import de.onlyfiles.model.Group;
import de.onlyfiles.model.User;
import de.onlyfiles.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired 
    PasswordEncoder passwordEncoder;

    @Operation(summary = "Create or update user",
            description = "Can create or update a user. Id in user object is only necessary if you want to update a user.")
    @PostMapping
    public ResponseEntity<User> createOrUpdateUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @Operation(summary = "Get user informations")
    @GetMapping(path = {"", "/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(Principal principal,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        
        User user = null;
        if(name.isPresent()) {
            user = userRepository.findByName(name.get());
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

    @Operation(summary = "Delete user")
    @DeleteMapping(path = {"", "/{name}"})
    public ResponseEntity<?> deleteUser(Principal principal,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        boolean success = false;
        
        if(name.isPresent()) {
            success = userRepository.deleteByName(name.get());
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

    @Operation(summary = "Get groups with files from user")
    @GetMapping(path = {"/groups", "/groups/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getGroups(Principal principal,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        
        User user = null;
        if(name.isPresent()) {
            user = userRepository.findByName(name.get());
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

    @Operation(summary = "Get owned groups with files")
    @GetMapping(path = {"/owned/groups/","/owned/groups/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getOwnedGroups(Principal principal,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        User user = null;
        
        if(name.isPresent()) {
            user = userRepository.findByName(name.get());
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

    @Operation(summary = "Get owned files")
    @GetMapping(path = {"/owned/files", "/owned/files/{name}"} , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<File>> getOwnedFiles(Principal principal,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        User user = null;

        if(name.isPresent()) {
            user = userRepository.findByName(name.get());
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

    @Operation(summary = "Get files where the user has access")
    @GetMapping(path = {"/files","/files/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<File>> getFiles(Principal principal,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        User user = null;
        
        if(name.isPresent()) {
            user = userRepository.findByName(name.get());
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
