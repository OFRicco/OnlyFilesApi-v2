package de.onlyfiles.repository;

import org.springframework.data.repository.CrudRepository;

import de.onlyfiles.model.File;

public interface FileRepository extends CrudRepository<File, Long> {
    public File findByName(String name);
    public boolean existsByLink(String link);
    public boolean deleteByName(String name);

    public File findFileById(Long id);
    public boolean deleteFileById(Long id);
}
