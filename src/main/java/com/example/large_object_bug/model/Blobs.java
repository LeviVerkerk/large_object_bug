package com.example.large_object_bug.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Blobs {

    @Id
    @GeneratedValue
    private String id;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "loid")
    private List<ObjectRecord> content;
}
