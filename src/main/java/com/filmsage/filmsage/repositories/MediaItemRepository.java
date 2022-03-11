package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.MediaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaItemRepository extends JpaRepository<MediaItem, Long> {
    MediaItem findByImdb(String imdb);
    boolean existsByImdb(String imdb);
}
