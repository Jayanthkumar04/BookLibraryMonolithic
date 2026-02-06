package com.org.jayanth.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.org.jayanth.dto.ReviewDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.ReviewResponseDto;
import com.org.jayanth.entity.Review;
import com.org.jayanth.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    public ReviewController(ReviewService service) {
        this.reviewService = service;
    }

    // ---------------------- ADD REVIEW ----------------------
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Review> addReview(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody ReviewDto dto) {

        logger.info(
                "Add review request initiated | user={} bookId={}",
                user.getUsername(), dto.getBookId()
        );

        Review review = reviewService.addReview(user.getUsername(), dto);

        logger.info(
                "Add review request successful | user={} reviewId={}",
                user.getUsername(), review.getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    // ---------------------- UPDATE REVIEW ----------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Review> updateReview(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id,
            @RequestBody ReviewDto dto) {

        logger.info(
                "Update review request initiated | user={} reviewId={}",
                user.getUsername(), id
        );

        Review review = reviewService.updateReview(user.getUsername(), id, dto);

        logger.info(
                "Update review request successful | user={} reviewId={}",
                user.getUsername(), id
        );

        return ResponseEntity.status(HttpStatus.OK).body(review);
    }

    // ---------------------- DELETE REVIEW ----------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MessageDto> deleteReview(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {

        logger.info(
                "Delete review request initiated | user={} reviewId={}",
                user.getUsername(), id
        );

        MessageDto message = reviewService.deleteReview(user.getUsername(), id);

        logger.info(
                "Delete review request successful | user={} reviewId={}",
                user.getUsername(), id
        );

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // ---------------------- GET REVIEWS BY BOOK ----------------------
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDto>> getBookReviews(@PathVariable Long bookId) {

        logger.info(
                "Get reviews by book request initiated | bookId={}",
                bookId
        );

        List<ReviewResponseDto> list = reviewService.getReviewsByBook(bookId);

        logger.info(
                "Get reviews by book request successful | bookId={} totalReviews={}",
                bookId, list.size()
        );

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
