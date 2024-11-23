package com.jatin.online_banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jatin.online_banking.model.User;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String username);

}
