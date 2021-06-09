package com.example.large_object_bug;

import ch.unibe.jexample.JExample;
import com.example.large_object_bug.controller.FileContentController;
import com.example.large_object_bug.model.File;
import com.example.large_object_bug.repository.FileRepository;
import com.example.large_object_bug.stores.FileContentStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

@SpringBootTest( classes = LargeObjectBugApplication.class )
@AutoConfigureMockMvc
public class LargeObjectBugApplicationTests {

    @Value("${spring.content.fs.filesystem-root}")
    private String path;

    @Autowired
    private FileRepository filesRepo;

    @Autowired
    private FileContentStore contentStore;

    @Test
    void unsetContentLeavesPostgresBLOB() throws Exception {
        // Given...
        // ...the initial number of records Spring Content manages in its table
        int initialCountSpringContentBlobs = filesRepo.countSpringContentBlobs();
        // ... and the number of large objects postgres currently has
        int initialCountPostgresLargeObjects = filesRepo.countLargeObjects();

        // ... A File record stored
        final File savedFile = filesRepo.save( new File(1L, "Test", new Date(), "Sum" ) );
        // ... and associated to some binary content
        MockMultipartFile file = new MockMultipartFile("data", "dummy.txt", "text/plain", "Some dataset...".getBytes());
        contentStore.setContent(savedFile, file.getInputStream());
        // save updated content-related info
        filesRepo.save(savedFile);
        Assertions.assertTrue(filesRepo.countSpringContentBlobs() > 0, "The file does not seem to have been stored in spring content blobs");
        Assertions.assertTrue(filesRepo.countLargeObjects() > initialCountPostgresLargeObjects, "The file does not seem to have been stored as postgres LO");

        // When... the association is removed
        contentStore.unsetContent(savedFile);
        filesRepo.save( savedFile );

        // Then
        // ... SpringContent removes the row from the blobs table
        Assertions.assertEquals( initialCountSpringContentBlobs, filesRepo.countSpringContentBlobs(), "The file does not seem to have been removed from spring content blobs" );
        // ... and removes the binary data from postgres's large object store
        Assertions.assertEquals( initialCountPostgresLargeObjects, filesRepo.countLargeObjects(), "The file does not seem to have been removed from postgres LO" );
    }
}
