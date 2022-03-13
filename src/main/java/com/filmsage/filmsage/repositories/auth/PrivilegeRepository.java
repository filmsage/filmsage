package com.filmsage.filmsage.repositories.auth;

import com.filmsage.filmsage.models.auth.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
