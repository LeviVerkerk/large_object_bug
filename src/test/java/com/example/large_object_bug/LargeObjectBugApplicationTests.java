package com.example.large_object_bug;

import ch.unibe.jexample.JExample;
import com.example.large_object_bug.controller.FileContentController;
import com.example.large_object_bug.repository.FileRepository;
import com.example.large_object_bug.stores.FileContentStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

@RunWith(JExample.class)
@SpringBootTest(classes = LargeObjectBugApplication.class)
@AutoConfigureMockMvc
class LargeObjectBugApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FileContentController fileContentController;

    @Value("${spring.content.fs.filesystem-root}")
    private String path;

    @Autowired
    private FileRepository filesRepo;

    @Autowired
    private FileContentStore contentStore;

    @Test
    @Transactional
    void fileAdd() throws Exception {

        MockMultipartFile file = new MockMultipartFile("data", "dummy.txt",
                "text/plain", "Some dataset...".getBytes());

        //  When
        filesRepo.save(new com.example.large_object_bug.model.File("Test", new Date(), "Sum"));

        Optional<com.example.large_object_bug.model.File> f = filesRepo.findById(1L);
        System.out.println(f);
        if (f.isPresent()) {
            f.get().setMimeType(file.getContentType());

            contentStore.setContent(f.get(), file.getInputStream());

            // save updated content-related info
            filesRepo.save(f.get());

            //  Then
            Assertions.assertTrue(filesRepo.countLargeObject() > 0, "There are more than 0 records in the largeobject table");
        }

        //  File lives in the Database
        //  TODO: Delete BLOB manually
        filesRepo.blobIds().forEach(n -> filesRepo.unlinkObjectId(n.intValue()));


        //  Commit all changes
        TestTransaction.flagForCommit();
        TestTransaction.end();

        //  Try and retrieve the file 10 times
        for (int i = 0; i < 10; i++) {
            try {
                if (f.isPresent()) {
                    InputStream testFile = contentStore.getContent(f.get());
                    if (testFile == null) {
                        throw new Exception("null was returned instead of the file, because connection pools are all occupied");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }

    @Test
    void fileRemove() {

        //  Given
        Optional<com.example.large_object_bug.model.File> f = filesRepo.findById(1L);

        //  When
        f.ifPresent(file -> contentStore.unsetContent(file));

        //  Then
        Assertions.assertEquals(0, filesRepo.countLargeObject(), "There are 0 records in the largeobject table");
    }

}
