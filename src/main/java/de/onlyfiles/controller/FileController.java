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
import de.onlyfiles.exception.ObjectAlreadyExistsException;
import de.onlyfiles.model.File;
import de.onlyfiles.model.Group;
import de.onlyfiles.repository.FileRepository;

@RestController
@RequestMapping("api/file")
public class FileController {

    @Autowired
    public FileRepository fileRepository;

    @PostMapping
    public ResponseEntity<File> createFile(@RequestBody File file) {
        if(fileRepository.existsByLink(file.getLink())) {
            throw new ObjectAlreadyExistsException();
        }
        
        File createdFile = fileRepository.save(file);
        
        return new ResponseEntity<>(createdFile, HttpStatus.OK);
    }
    
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<File> getFile(@PathVariable(value="id", required = true) Long id) {
        File file = fileRepository.findFileById(id);
        
        if(file == null) {
            throw new FileNotFoundException();
        }
        
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@PathVariable(value="id", required = true) Long id) {
        boolean success = fileRepository.deleteFileById(id);
        
        if(success) {
            throw new DeleteFailedException();
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Group>> getRelatedGroups(@PathVariable(value="id", required = true) Long id) { // add a dynamic json filter for only showing groups
        File file = fileRepository.findFileById(id);
        
        if(file == null) {
            throw new FileNotFoundException();
        }
        
        Set<Group> groups = file.getGroups();
        
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}
