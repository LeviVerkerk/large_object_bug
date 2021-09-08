package com.example.large_object_bug.repository;

import com.example.large_object_bug.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="files", collectionResourceRel="files")
public interface FileRepository extends JpaRepository<File, Long> {

    @Query(value = "SELECT COUNT(*) FROM pg_largeobject"
    , nativeQuery = true)
    int countLargeObject();

    @Query(value = "SELECT lo_unlink(?1)"
    , nativeQuery = true)
    void unlinkObjectId(int id);

    @Query(value = "SELECT id FROM blobs"
    , nativeQuery = true)
    List<Long> blobIds();

}
