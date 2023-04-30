package com.example.twitterauthservice.credentials;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface credentialsRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
