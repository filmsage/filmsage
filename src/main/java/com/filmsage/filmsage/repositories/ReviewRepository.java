package com.filmsage.filmsage.repositories;

import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMediaItem(MediaItem mediaItem);
    List<Review> findAllByMediaItemImdb(String imdb);
}
