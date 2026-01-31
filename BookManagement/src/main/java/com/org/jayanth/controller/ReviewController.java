package com.org.jayanth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.org.jayanth.dto.ReviewDto;
import com.org.jayanth.dtobestprac.ReviewResponseDto;
import com.org.jayanth.entity.Review;
import com.org.jayanth.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService service) {
        this.reviewService = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> addReview(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody ReviewDto dto) {

        return ResponseEntity.ok(reviewService.addReview(user.getUsername(), dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id,
            @RequestBody ReviewDto dto) {

        return ResponseEntity.ok(reviewService.updateReview(user.getUsername(), id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {

        reviewService.deleteReview(user.getUsername(), id);
        return ResponseEntity.ok("Review Deleted");
    }

    @GetMapping("/book/{bookId}")
    public List<ReviewResponseDto> getBookReviews(@PathVariable Long bookId) {
        return reviewService.getReviewsByBook(bookId);
    }
}
