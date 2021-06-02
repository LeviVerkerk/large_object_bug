package com.example.large_object_bug;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;
import com.example.large_object_bug.repository.FileFSRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JExample.class)
@SpringBootTest(classes = LargeObjectBugApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
class LargeObjectBugApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FileContentController fileContentController;

    @Value("${spring.content.fs.filesystem-root}")
    private String path;

    @Autowired
    private FileFSRepository filesRepo;

    @Autowired
    private FileContentStore contentStore;

    @Test
    void fileAdd() throws Exception {

        MockMultipartFile file = new MockMultipartFile("data", "dummy.txt",
                "text/plain", "Some dataset...".getBytes());

        filesRepo.save(new com.example.large_object_bug.File("Test", new Date(), "Sum"));

        Optional<com.example.large_object_bug.File> f = filesRepo.findById(1L);
        System.out.println(f);
        if (f.isPresent()) {
            f.get().setMimeType(file.getContentType());

            contentStore.setContent(f.get(), file.getInputStream());

            // save updated content-related info
            filesRepo.save(f.get());

            Assertions.assertTrue(new File(this.path).exists());
        }
    }

    @Test
    void fileRemove() throws InterruptedException {

        Thread.sleep(1000);

        Optional<com.example.large_object_bug.File> f = filesRepo.findById(1L);
        f.ifPresent(file -> contentStore.unsetContent(file));

        //  Then
        Assertions.assertFalse(new File(this.path).exists());
    }

}
