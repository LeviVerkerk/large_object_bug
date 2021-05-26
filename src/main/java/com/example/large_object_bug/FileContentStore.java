package com.example.large_object_bug;

import org.springframework.content.commons.repository.ContentStore;

public interface FileContentStore extends ContentStore<File, String> {
}
