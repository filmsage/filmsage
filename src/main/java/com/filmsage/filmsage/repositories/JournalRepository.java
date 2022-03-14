package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.Journal;
import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalRepository  extends JpaRepository<Journal, Long> {
    List<Journal> findAllByUserContent_User(User user);
    List<Journal> findAllByUserContent_Id(long id);
}
