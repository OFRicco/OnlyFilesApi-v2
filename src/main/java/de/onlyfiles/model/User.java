package de.onlyfiles.model;

import java.util.ArrayList;
import java.util.List;

import de.onlyfiles.Permission;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UNSIGNED BIGINT", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "name", columnDefinition = "VARCHAR(32)", nullable = false, unique = true, updatable = true)
    private String name;

    @Column(name = "password", columnDefinition = "VARCHAR(60)", nullable = false, unique = false, updatable = true)
    private String password;
    
    @OneToMany(targetEntity = Group.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Group> ownedGroups;

    @OneToMany(targetEntity = File.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<File> ownedFiles = new ArrayList<>();
    
    @Column(name = "permission", nullable = false, unique = false, updatable = true)
    @Enumerated(EnumType.STRING)
    private Permission permission;

    public User() {
    }
    
    public User(String name, String password, Permission permission) {
        this.name = name;
        this.password = password;
        this.permission = permission;
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String displayName) {
        this.name = displayName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(List<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public List<File> getOwnedFiles() {
        return ownedFiles;
    }

    public void setOwnedFiles(List<File> ownedFiles) {
        this.ownedFiles = ownedFiles;
    }

    public Permission getPermission() {
        return permission;
    }
    
    public void setPermission(Permission permissions) {
        this.permission = permissions;
    }
    
    public void addOwnedFile(File newFile) {
        this.ownedFiles.add(newFile);
    }

}
