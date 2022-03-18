package com.filmsage.filmsage.controllers;

import com.filmsage.filmsage.repositories.ReviewRepository;
import com.filmsage.filmsage.services.LikesService;
import com.filmsage.filmsage.services.UserContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {
    
    private UserContentService userContentService;
    private ReviewRepository reviewDao;
    private LikesService likesService;

    public LikeController(UserContentService userContentService, ReviewRepository reviewDao, LikesService likesService) {
        this.userContentService = userContentService;
        this.reviewDao = reviewDao;
        this.likesService = likesService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/like")
    public ResponseEntity<?> likeReview(@RequestParam long r) {
        long result = likesService.likeReview(r);
        return ResponseEntity.ok(result);
    }
}
