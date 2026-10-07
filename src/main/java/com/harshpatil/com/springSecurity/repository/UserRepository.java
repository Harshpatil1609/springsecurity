package com.harshpatil.com.springSecurity.repository;

import com.harshpatil.com.springSecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User ,Long> {

    Optional<User> findByEmail(String email);
}
