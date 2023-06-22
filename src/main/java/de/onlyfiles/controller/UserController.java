package de.onlyfiles.controller;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.onlyfiles.exception.DeleteFailedException;
import de.onlyfiles.exception.MySQLException;
import de.onlyfiles.exception.NoCurrentPrincipalException;
import de.onlyfiles.exception.UserNotFoundException;
import de.onlyfiles.model.File;
import de.onlyfiles.model.Group;
import de.onlyfiles.model.User;
import de.onlyfiles.repository.UserRepository;
import de.onlyfiles.security.MySQLUser;
import de.onlyfiles.security.MySQLUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User")
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Autowired
    private MySQLUserDetailsService mySQLUserDetailsService;
    
    @Operation(summary = "Create user")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setId(0L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
    
    @Operation(summary = "Update user", description = "Need id of the user\nPermission: atleast mod or higher for higher qualified users.")
    @PutMapping(path = "/update")
    public ResponseEntity<User> updateUser(Principal principal, Authentication authentication, @RequestBody User reqUser) {
        
        User user = userRepository.findUserById(reqUser.getId());
        
        if(user == null) {
            throw new UserNotFoundException();
        }
        User curUser = userRepository.findByName(principal.getName());
        
        /*
        if(curUser.getId() != user.getId() && (curUser.getPermission().ordinal() > Permission.MOD.ordinal() || curUser.getPermission().ordinal() >= user.getPermission().ordinal())) {
            throw new InsufficientPermissionException();
        }
        */
        
        user.setName(reqUser.getName() != null ? reqUser.getName() : user.getName()); // guck ob der name nicht schon existiert
        user.setPassword(reqUser.getPassword() != null ? passwordEncoder.encode(reqUser.getPassword()) : user.getPassword());
        user.setPermission(reqUser.getPermission() != null ? reqUser.getPermission() : user.getPermission());
        
        User newUser = userRepository.save(user);
        
        if(newUser == null) {
            throw new MySQLException();
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getPermission().toString()));

        for(Object p : sessionRegistry.getAllPrincipals()) {
            if(((MySQLUser) p).getId() == ((MySQLUser) authentication.getPrincipal()).getId()) {
                for(SessionInformation sessionInformation : sessionRegistry.getAllSessions(p, false)) {
                    /**
                     * For re-auth we need credentials, but with the hashed password we dont know the plain password
                     * Just relogin, its easier and safer
                     */
                    sessionInformation.expireNow();
                }
            }
        }
        
        return new ResponseEntity<>(reqUser, HttpStatus.OK);
    }

    @Operation(summary = "Get user informations")
    @GetMapping(path = {"", "/{name}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(Principal principal, Authentication authentication,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(name.isPresent()) {
            user = userRepository.findByName(name.get());
        } else {
            if(principal == null) {
                throw new NoCurrentPrincipalException();
            }
            
            user = userRepository.findByName(principal.getName());
        }
        
        for(User user1 : userRepository.findAll()) {
            System.out.println(user1.getName());
        }
        
        if(user == null) {
            throw new UserNotFoundException();
        }
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Delete user")
    @DeleteMapping(path = {"", "/{name}"})
    public ResponseEntity<?> deleteUser(Principal principal, Authentication authentication,
            @PathVariable(value="name", required = false) @Parameter(name = "name", description = "The user name") Optional<String> name) {
        boolean success = false;
        

        /*
        if(curUser.getPermission().ordinal() < Permission.MOD.ordinal()) {
            throw new InsufficientPermissionException();
        }
        */
        
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
