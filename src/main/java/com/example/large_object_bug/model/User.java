package com.example.large_object_bug.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String username;

    @ContentId
    private UUID contentId;

    @ContentLength
    private Long contentLength;

    @MimeType
    private String mimeType;

    private String contentPath;

    public User(String username) {
        this.username = username;
    }
}
