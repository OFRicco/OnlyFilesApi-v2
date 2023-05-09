package de.onlyfiles.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.onlyfiles.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public User findUserByName(String name);
    public boolean existsByName(String name);
    public boolean deleteByName(String name);
}
