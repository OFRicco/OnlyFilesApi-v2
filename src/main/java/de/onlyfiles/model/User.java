package de.onlyfiles.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer","ownedGroups","ownedFiles","groups"})
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "name", columnDefinition = "VARCHAR(32)", nullable = false, unique = true, updatable = true)
    private String name;

    @Column(name = "password", columnDefinition = "VARCHAR(60)", nullable = false, unique = false, updatable = true)
    private String password;

    @OneToMany(targetEntity = Group.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Group> ownedGroups = new HashSet<>();

    @OneToMany(targetEntity = File.class, mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<File> ownedFiles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "group_users",
            joinColumns = @JoinColumn(name="group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private Set<Group> groups = new HashSet<>();
    
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

    public void setId(Long id) {
        this.id = id;
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
    
    public Set<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void setOwnedGroups(Set<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public Set<File> getOwnedFiles() {
        return ownedFiles;
    }

    public void setOwnedFiles(Set<File> ownedFiles) {
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
    
    public Set<Group> getGroups() {
        return groups;
    }
    
    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
    
    
}
