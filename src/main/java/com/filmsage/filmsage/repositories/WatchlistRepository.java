package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Collection<Watchlist> findWatchlistsByUserContent_Id(long id);
    Collection<Watchlist> findWatchlistsByUserContent(UserContent userContent);
}
