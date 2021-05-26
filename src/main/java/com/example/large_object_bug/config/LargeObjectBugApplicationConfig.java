package com.example.large_object_bug.config;

import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
@EnableFilesystemStores
public class LargeObjectBugApplicationConfig {

    @Bean
    File filesystemRoot() {
        try {
            return Files.createTempDirectory("").toFile();
        } catch (IOException ioe) {}
        return null;
    }

    @Bean
    FileSystemResourceLoader fileSystemResourceLoader() {
        return new FileSystemResourceLoader();
    }

}
