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

import de.onlyfiles.exception.DeleteFailedException;
import de.onlyfiles.exception.FileNotFoundException;
import de.onlyfiles.exception.GroupNotFoundException;
import de.onlyfiles.exception.ObjectContainsException;
import de.onlyfiles.exception.UserNotFoundException;
import de.onlyfiles.model.File;
import de.onlyfiles.model.Group;
import de.onlyfiles.model.User;
import de.onlyfiles.repository.FileRepository;
import de.onlyfiles.repository.GroupRepository;
import de.onlyfiles.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Group")
@RestController
@RequestMapping("api/group")
public class GroupController {
    
    @Autowired
    public FileRepository fileRepository;
    
    @Autowired
    public GroupRepository groupRepository;
    
    @Autowired
    public UserRepository userRepository;

    @Operation(summary = "Create or update group",
            description = "Can create or update a group. Id in group object is only necessary if you want to update a group.")
    @PostMapping
    public ResponseEntity<Group> createOrUpdateGroup(@RequestBody Group group) {
        
        Group createdGrouped = groupRepository.save(group);
        
        return new ResponseEntity<>(createdGrouped, HttpStatus.OK);
    }

    @Operation(summary = "Get group informations")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> getGroup(@PathVariable(value="id", required = true) @Parameter(name = "id", description = "The group id") Long id) {
        Group group = groupRepository.findGroupById(id);
        
        if(group == null) {
            throw new GroupNotFoundException();
        }
        
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    @Operation(summary = "Delete group")
    @DeleteMapping
    public ResponseEntity<?> deleteGroup(@PathVariable(value="id", required = true) @Parameter(name = "id", description = "The group id") Long id) {
        
        boolean success = groupRepository.deleteGroupById(id);

        if(success) {
            throw new DeleteFailedException();
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Add user to group")
    @PostMapping(path = "/{group_id}/{user_id}")
    public ResponseEntity<?> addMember(@PathVariable(value="group_id", required = true)  @Parameter(name = "group_id", description = "The group id") Long groupId,
            @PathVariable(value="user_id", required = true) @Parameter(name = "user_id", description = "The user id") Long userId) {
        
        Group group = groupRepository.findGroupById(groupId);
        
        if(group == null) {
            throw new GroupNotFoundException();
        }
        
        User user = userRepository.findUserById(userId);

        if(user == null) {
            throw new UserNotFoundException();
        }
        
        if(group.getMembers().contains(user)) {
            throw new ObjectContainsException();
        }
        
        group.addMember(user);
        groupRepository.save(group);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete user from group")
    @DeleteMapping(path = "/{group_id}/{user_id}")
    public ResponseEntity<?> removeMember(@PathVariable(value="group_id", required = true)  @Parameter(name = "group_id", description = "The group id") Long groupId,
            @PathVariable(value="user_id", required = true) @Parameter(name = "user_id", description = "The user id") Long userId) {
        
        Group group = groupRepository.findGroupById(groupId);
        
        if(group == null) {
            throw new GroupNotFoundException();
        }
        
        User user = userRepository.findUserById(userId);

        if(user == null) {
            throw new UserNotFoundException();
        }

        if(!group.getMembers().contains(user)) {
            throw new UserNotFoundException();
        }
        
        group.removeMember(user);
        groupRepository.save(group);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    

    @Operation(summary = "Add file to group")
    @PostMapping(path = "/{group_id}/{file_id}")
    public ResponseEntity<?> addFile(@PathVariable(value="group_id", required = true) @Parameter(name = "group_id", description = "The group id") Long groupId,
            @PathVariable(value="file_id", required = true) @Parameter(name = "file_id", description = "The file id") Long fileId) {

        Group group = groupRepository.findGroupById(groupId);
        
        if(group == null) {
            throw new GroupNotFoundException();
        }
        
        File file = fileRepository.findFileById(fileId);

        if(file == null) {
            throw new FileNotFoundException();
        }
        
        if(group.getFiles().contains(file)) {
            throw new ObjectContainsException();
        }
        
        group.addFile(file);
        groupRepository.save(group);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete file from group")
    @DeleteMapping(path = "/{group_id}/{file_id}")
    public ResponseEntity<?> removeFile(@PathVariable(value="group_id", required = true)  @Parameter(name = "group_id", description = "The group id") Long groupId,
            @PathVariable(value="file_id", required = true) @Parameter(name = "file_id", description = "The groupId id") Long fileId) {

        Group group = groupRepository.findGroupById(groupId);
        
        if(group == null) {
            throw new GroupNotFoundException();
        }
        
        File file = fileRepository.findFileById(fileId);

        if(file == null) {
            throw new FileNotFoundException();
        }

        if(!group.getFiles().contains(file)) {
            throw new FileNotFoundException();
        }
        
        group.removeFile(file);
        groupRepository.save(group);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
