package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.User;
import com.filmsage.filmsage.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
