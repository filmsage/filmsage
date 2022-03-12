package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
}

