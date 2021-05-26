package com.example.large_object_bug;

import com.example.large_object_bug.model.User;
import com.example.large_object_bug.repository.UserRepository;
import com.example.large_object_bug.store.ProfilePictureStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.FileInputStream;

@SpringBootApplication
public class LargeObjectBugApplication {

    public static void main(String[] args) {
        SpringApplication.run(LargeObjectBugApplication.class, args);
    }


    @Bean
    public CommandLineRunner demo(UserRepository repository, ProfilePictureStore store) {
        return (args) -> {
            //  create new user
            User levi = new User("levi");

            //  store profile picture
            store.setContent(levi, new FileInputStream("/tmp/levi.jpg"));

            //  save the user
            repository.save(levi);
        };
    }
}
