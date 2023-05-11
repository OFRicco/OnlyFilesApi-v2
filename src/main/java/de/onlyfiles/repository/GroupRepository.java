package de.onlyfiles.repository;

import org.springframework.data.repository.CrudRepository;

import de.onlyfiles.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
    public Group findByName(String name);
    public boolean existsByName(String name);
    public boolean deleteByName(String name);
}
