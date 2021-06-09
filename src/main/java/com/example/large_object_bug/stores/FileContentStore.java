package com.example.large_object_bug.stores;

import com.example.large_object_bug.model.File;
import org.springframework.content.commons.renditions.Renderable;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public interface FileContentStore extends ContentStore<File, String>, Renderable<File> {
}
