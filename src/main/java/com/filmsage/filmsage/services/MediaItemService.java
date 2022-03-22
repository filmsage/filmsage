package com.filmsage.filmsage.services;

import com.filmsage.filmsage.models.MediaItem;
import com.filmsage.filmsage.models.json.MediaItemMapped;
import com.filmsage.filmsage.repositories.MediaItemRepository;
import org.springframework.stereotype.Service;

@Service
public class MediaItemService {

    private OMDBRequester omdbRequester;
    private MediaItemRepository mediaItemDao;

    public MediaItemService(OMDBRequester omdbRequester, MediaItemRepository mediaItemDao) {
        this.omdbRequester = omdbRequester;
        this.mediaItemDao = mediaItemDao;
    }

    public MediaItem getMediaItemRecord(String imdb) {
        // if we have a record of this imdb in our system,
        if (mediaItemDao.existsByImdb(imdb)) {
            // we retrieve that record
            return mediaItemDao.findByImdb(imdb);
        } else {
            // if not, we make a new record and return that instead
            MediaItemMapped mediaItemMapped = omdbRequester.getMovie(imdb);
            MediaItem mediaItem = new MediaItem(
                    mediaItemMapped.getImdbID(),
                    mediaItemMapped.getTitle(),
                    mediaItemMapped.getYear(),
                    mediaItemMapped.getGenre(),
                    mediaItemMapped.getPoster().toString()
            );
            return mediaItemDao.save(mediaItem);
        }
    }

    public MediaItem getTempMediaItemRecord(String imdb) {
        // if we have a record of this imdb in our system,
        if (mediaItemDao.existsByImdb(imdb)) {
            // we retrieve that record
            return mediaItemDao.findByImdb(imdb);
        } else {
            MediaItemMapped mediaItemMapped = omdbRequester.getMovie(imdb);
            return new MediaItem(
                    mediaItemMapped.getImdbID(),
                    mediaItemMapped.getTitle(),
                    mediaItemMapped.getYear(),
                    mediaItemMapped.getGenre(),
                    mediaItemMapped.getPoster().toString()
            );
        }
    }
}
