package de.onlyfiles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import de.onlyfiles.model.Group;
import de.onlyfiles.repository.GroupRepository;

@RestController
@RequestMapping("api/group")
public class GroupController {

    @Autowired
    public GroupRepository groupRepository;

    @PostMapping
    public ResponseEntity<Long> createGroup(@RequestBody Group group) {
        if(groupRepository.existsByName(group.getName())) {
            throw new ObjectAlreadyExistsException();
        }
        
        Group createdGrouped = groupRepository.save(group);
        
        return new ResponseEntity<>(createdGrouped.getId(), HttpStatus.OK);
    }

    @GetMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> getGroup(@PathVariable(value="name", required = true) String name) {
        Group group = groupRepository.findByName(name);
        
        if(group != null) {
            throw new UserNotFoundException();
        }
        
        return new ResponseEntity<>(group, HttpStatus.OK);
    }
    
    @DeleteMapping
    public ResponseEntity<Group> deleteGroup(@PathVariable(value="name") String name) {
        if(name != null) {
            throw new NoCurrentPrincipalException();
        }
        
        Group group = groupRepository.findByName(name);
        
        return new ResponseEntity<>(group, HttpStatus.OK);
    }
}
