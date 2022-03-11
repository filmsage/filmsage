package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.Journal;
import com.filmsage.filmsage.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository  extends JpaRepository<Journal, Long> {
}
