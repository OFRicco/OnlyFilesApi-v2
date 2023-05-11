package de.onlyfiles.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties(value = {"members"})
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "name", columnDefinition = "VARCHAR(32)", nullable = false, unique = true, updatable = true)
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "group_users",
            joinColumns = @JoinColumn(name="group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "group_files",
            joinColumns = @JoinColumn(name="group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="file_id", referencedColumnName = "id"))
    private Set<File> files = new HashSet<>();

    public Group() {
    }

    public Group(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public Group(String name, User owner, Set<User> members, Set<File> files) {
        this.name = name;
        this.owner = owner;
        this.members = members;
        this.files = files;
    }

    public boolean addMember(User newMember) {
        return this.members.add(newMember);
    }

    public boolean removeMember(User member) {
        return this.members.remove(member);
    }
    
    public boolean addFile(File newFile) {
        return this.files.add(newFile);
    }
    
    public boolean removeFile(File file) {
        return this.files.add(file);
    }
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public Set<User> getMembers() {
        return members;
    }
    
    public void setMembers(Set<User> members) {
        this.members = members;
    }
    
    public Set<File> getFiles() {
        return files;
    }
    
    public void setFiles(Set<File> files) {
        this.files = files;
    }
    
}
