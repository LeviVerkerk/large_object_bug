package com.example.large_object_bug.repository;

import com.example.large_object_bug.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
