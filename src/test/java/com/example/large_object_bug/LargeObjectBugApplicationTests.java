package com.example.large_object_bug;

import ch.unibe.jexample.JExample;
import com.example.large_object_bug.controller.FileContentController;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
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
    void fileAdd() throws Exception {

        MockMultipartFile file = new MockMultipartFile("data", "dummy.txt",
                "text/plain", "Some dataset...".getBytes());

        filesRepo.save(new com.example.large_object_bug.model.File("Test", new Date(), "Sum"));

        Optional<com.example.large_object_bug.model.File> f = filesRepo.findById(1L);
        System.out.println(f);
        if (f.isPresent()) {
            f.get().setMimeType(file.getContentType());

            contentStore.setContent(f.get(), file.getInputStream());

            // save updated content-related info
            filesRepo.save(f.get());

            Assertions.assertTrue(filesRepo.findContent(f.get().getId()) > 0, "There are more than 0 records in the largeobject table");
        }
    }

    @Test
    void fileRemove() throws InterruptedException {

        Thread.sleep(1000);

        Optional<com.example.large_object_bug.model.File> f = filesRepo.findById(1L);
        f.ifPresent(file -> contentStore.unsetContent(file));

        //  Then
        Assertions.assertTrue(filesRepo.findContent(f.get().getId()) == 0, "There are 0 records in the largeobject table");
    }

}
