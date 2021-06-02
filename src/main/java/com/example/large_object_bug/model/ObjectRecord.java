package com.example.large_object_bug.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "pg_largeobject")
@NoArgsConstructor
public class ObjectRecord {

    @Id
    @GeneratedValue
    private long loid;

    private int pageNo;

    private byte[] data;
}
