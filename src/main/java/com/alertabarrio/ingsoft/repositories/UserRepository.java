package com.alertabarrio.ingsoft.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alertabarrio.ingsoft.models.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
