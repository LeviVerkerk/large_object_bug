package com.example.large_object_bug.repository;

import com.example.large_object_bug.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="files", collectionResourceRel="files")
public interface FileFSRepository extends JpaRepository<File, Long> {

}
