package com.filmsage.filmsage.repositories.auth;

import com.filmsage.filmsage.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
