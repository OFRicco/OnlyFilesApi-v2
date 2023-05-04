package de.onlyfiles.repository;

import org.springframework.data.repository.CrudRepository;

import de.onlyfiles.model.File;

public interface FileRepository extends CrudRepository<File, Long> {

}
