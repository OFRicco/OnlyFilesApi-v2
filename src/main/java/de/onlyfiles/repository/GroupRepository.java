package de.onlyfiles.repository;

import org.springframework.data.repository.CrudRepository;

import de.onlyfiles.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {

}
