package com.example.large_object_bug.store;

import com.example.large_object_bug.model.User;
import org.springframework.content.commons.annotations.Content;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

public interface ProfilePictureStore extends ContentStore<User, UUID> {
}
