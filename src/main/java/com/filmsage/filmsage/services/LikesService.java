package com.filmsage.filmsage.services;

import com.filmsage.filmsage.models.Review;
import com.filmsage.filmsage.models.UserContent;
import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.repositories.UserContentRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    private UserContentRepository userContentDao;
    private UserContentService userContentService;
    private ReviewRepository reviewDao;

    public LikesService(UserContentRepository userContentDao, ReviewRepository reviewDao, UserContentService userContentService) {
        this.userContentDao = userContentDao;
        this.userContentService = userContentService;
        this.reviewDao = reviewDao;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public long likeReview(long reviewId) {
        UserContent user = userContentService.getUserContent();
        Review review = reviewDao.findReviewById(reviewId);
        if (!user.getLikedReviews().contains(review)) {
            user.getLikedReviews().add(review);
            System.out.println("________ ADDED LIKE ________");
        } else {
            user.getLikedReviews().remove(review);
            System.out.println("________ REMOVED LIKE ________");
        }
        userContentService.save(user);
        return review.getUserLikes().size();
    }

}
