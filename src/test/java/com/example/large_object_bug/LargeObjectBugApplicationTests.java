package com.example.large_object_bug;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LargeObjectBugApplication.class)
@AutoConfigureMockMvc
class LargeObjectBugApplicationTests {

    @Autowired
    private FileContentController fileContentController;

    @Value("${spring.content.fs.filesystem-root}")
    private String path;

    @Test
    void testIfFileIsRemoved() throws IOException {

        //  Given
        Path path = Paths.get("/file/file.txt");
        String name = "file.txt";
        String originalFileName = "file.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, content);

        fileContentController.setContent(1L, file);

        //  When
        fileContentController.deleteContent(1L);

        //  Then
        Assertions.assertFalse(new File(this.path).exists());
    }

}
