package com.example.large_object_bug.repository;

import com.example.large_object_bug.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="files", collectionResourceRel="files")
public interface FileRepository extends JpaRepository<File, Long> {

    @Query(value = "SELECT COUNT(*) FROM pg_catalog.pg_largeobject_metadata"
    , nativeQuery = true)
    int countLargeObjects();

    @Query(value = "SELECT COUNT(*) FROM blobs"
    , nativeQuery = true)
    int countSpringContentBlobs();


}
