package de.onlyfiles.controller;

import java.util.Set;

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
import de.onlyfiles.model.File;
import de.onlyfiles.model.Group;
import de.onlyfiles.repository.FileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File")
@RestController
@RequestMapping("api/file")
public class FileController {

    @Autowired
    public FileRepository fileRepository;

    @Operation(summary = "Create or update file",
            description = "Can create or update a file. Id in file object is only necessary if you want to update a file.")
    @PostMapping
    public ResponseEntity<File> createOrUpdateFile(@RequestBody File file) {
        File createdFile = fileRepository.save(file);
        
        return new ResponseEntity<>(createdFile, HttpStatus.OK);
    }

    @Operation(summary = "Get file informations")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<File> getFile(@PathVariable(value="id", required = true) @Parameter(name = "id", description = "The file id") Long id) {
        File file = fileRepository.findFileById(id);
        
        if(file == null) {
            throw new FileNotFoundException();
        }
        
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @Operation(summary = "Delete file")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@PathVariable(value="id", required = true) @Parameter(name = "id", description = "The file id") Long id) {
        boolean success = fileRepository.deleteFileById(id);
        
        if(success) {
            throw new DeleteFailedException();
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get a list of related groups", description = "Get a list from all groups, where the file is connected.")
    @GetMapping(path = "/groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getRelatedGroups(@PathVariable(value="id", required = true) @Parameter(name = "id", description = "The file id") Long id) {
        File file = fileRepository.findFileById(id);
        
        if(file == null) {
            throw new FileNotFoundException();
        }
        
        Set<Group> groups = file.getGroups();
        
        for(Group group : groups) {
            System.out.println(group.getOwner().getName());
        }
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}
