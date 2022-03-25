package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findWatchlistsByUserContent_Id(long id);
    List<Watchlist> findWatchlistsByUserContent(UserContent userContent);
    List<Watchlist> findAllByMediaItems_Imdb(String imdb);
}
