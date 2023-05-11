package de.onlyfiles.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.onlyfiles.Permission;
import de.onlyfiles.model.File;
import de.onlyfiles.model.Group;
import de.onlyfiles.model.User;
import de.onlyfiles.repository.FileRepository;
import de.onlyfiles.repository.GroupRepository;
import de.onlyfiles.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/")
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private FileRepository fileRepository;

    @GetMapping
    public String getTester(HttpServletRequest request) {

        return "test";
    }
    
    @GetMapping(path = "/create/user")
    public String createTestUser() {
        userRepository.save(new User("ben","$2a$12$SHZXuZIDczUGCw8mt4lhu.lr97JeuETk3blBou1FdyO2KyVF0cb0y",Permission.ADMIN));
        return "created";
    }
    

    @GetMapping(path = "/create")
    public String createTestGroup() {

        if(!userRepository.existsByName("ben"))
            userRepository.save(new User("ben","$2a$12$SHZXuZIDczUGCw8mt4lhu.lr97JeuETk3blBou1FdyO2KyVF0cb0y",Permission.ADMIN));
        
        User ben = userRepository.findByName("ben");
        
        if(!groupRepository.existsByName("bens group")) {
            Group myGroup = new Group("bens group", ben);
            groupRepository.save(myGroup);
        }
        
        Group group = groupRepository.findByName("bens group");
        if(group.getMembers().isEmpty())
            group.addMember(ben);
        
        File file = new File("testFile", ben, "www.iwas", 4L);
        
        fileRepository.save(file);
        if(group.getFiles().isEmpty())
            group.addFile(file);
        
        groupRepository.save(group);
        
        
        return "created";
    }
    

    @GetMapping(path = "/info/group")
    public String getUserInfo() {

        File f = fileRepository.findByName("testFile");
        Group g = groupRepository.findByName("bens group");
        
        User u = userRepository.findByName("ben");
        
        for(User member : g.getMembers()) {
            System.out.println(member.getName());
        }
        
        System.out.println(u.getGroups().size());
        
        for(Group group : u.getGroups()) {
            System.out.println(group.getName());
        }
        
        return "in console";
    }
    
}
