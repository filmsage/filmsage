package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Watchlist, Long> {
}
