package com.org.jayanth.service;

import java.util.List;

import com.org.jayanth.dto.ReviewDto;
import com.org.jayanth.dtobestprac.ReviewResponseDto;
import com.org.jayanth.entity.Review;

public interface ReviewService {

    Review addReview(String email, ReviewDto dto);

    Review updateReview(String email, Long reviewId, ReviewDto dto);

    void deleteReview(String email, Long reviewId);

    List<ReviewResponseDto> getReviewsByBook(Long bookId);

    List<Review> getReviewsByUser(Long userId);
}
