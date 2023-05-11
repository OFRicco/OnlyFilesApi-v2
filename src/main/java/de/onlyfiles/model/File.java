package de.onlyfiles.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "name", columnDefinition = "VARCHAR(256)", nullable = false, unique = false, updatable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
 
    @Column(name = "link", columnDefinition = "VARCHAR(128)", nullable = false, unique = true, updatable = false)
    private String link;

    @Column(name = "size", columnDefinition = "BIGINT UNSIGNED", nullable = false, unique = false, updatable = false)
    private Long size;
    
    public File() {
    }
    
    public File(String name, User owner, String link, Long size) {
        this.name = name;
        this.owner = owner;
        this.link = link;
        this.size = size;
    }

    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
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
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    
}
