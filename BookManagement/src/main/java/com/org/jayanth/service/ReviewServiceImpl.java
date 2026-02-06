package com.org.jayanth.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.ReviewDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.dtobestprac.ReviewResponseDto;
import com.org.jayanth.entity.Book;
import com.org.jayanth.entity.Review;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.BookNotFoundException;
import com.org.jayanth.exceptions.ReviewNotFoundException;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.exceptions.UserUnauthorizedPageException;
import com.org.jayanth.repo.BookRepo;
import com.org.jayanth.repo.OrderItemRepo;
import com.org.jayanth.repo.ReviewRepo;
import com.org.jayanth.repo.UserRepo;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Override
    public Review addReview(String email, ReviewDto dto) {
        logger.info("Add review initiated | email={} bookId={}", email, dto.getBookId());

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found | email={}", email);
                    return new UserNotFoundException("User not found");
                });

        Book book = bookRepo.findById(dto.getBookId())
                .orElseThrow(() -> {
                    logger.error("Book not found | bookId={}", dto.getBookId());
                    return new BookNotFoundException("Book Not Found");
                });

        if (!orderItemRepo.hasUserPurchasedBook(user.getId(), dto.getBookId())) {
            logger.error("Unauthorized review attempt | userId={} bookId={}", user.getId(), dto.getBookId());
            throw new UserUnauthorizedPageException("You can only review books you purchased!");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(dto.getRating());
        review.setComment(dto.getComments());

        Review saved = reviewRepo.save(review);
        logger.info("Review added successfully | reviewId={} userId={} bookId={}", saved.getId(), user.getId(), book.getId());
        return saved;
    }

    @Override
    public Review updateReview(String email, Long reviewId, ReviewDto dto) {
        logger.info("Update review initiated | email={} reviewId={}", email, reviewId);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found | email={}", email);
                    return new UserNotFoundException("User Not Found");
                });

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> {
                    logger.error("Review not found | reviewId={}", reviewId);
                    return new ReviewNotFoundException("Review Not Found");
                });

        if (!review.getUser().getId().equals(user.getId())) {
            logger.error("Unauthorized review update attempt | userId={} reviewId={}", user.getId(), reviewId);
            throw new UserUnauthorizedPageException("You cannot edit someone else’s review");
        }

        if (dto.getRating() != null) review.setRating(dto.getRating());
        if (dto.getComments() != null) review.setComment(dto.getComments());

        Review updated = reviewRepo.save(review);
        logger.info("Review updated successfully | reviewId={} userId={}", updated.getId(), user.getId());
        return updated;
    }

    @Override
    public MessageDto deleteReview(String email, Long reviewId) {
        logger.info("Delete review initiated | email={} reviewId={}", email, reviewId);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found | email={}", email);
                    return new UserNotFoundException("User Not Found");
                });

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> {
                    logger.error("Review not found | reviewId={}", reviewId);
                    return new ReviewNotFoundException("Review Not Found");
                });

        if (!review.getUser().getId().equals(user.getId())) {
            logger.error("Unauthorized review deletion attempt | userId={} reviewId={}", user.getId(), reviewId);
            throw new UserUnauthorizedPageException("You cannot delete someone else’s review");
        }

        reviewRepo.delete(review);
        logger.info("Review deleted successfully | reviewId={} userId={}", reviewId, user.getId());
        return new MessageDto("Review has been deleted successfully");
    }

    @Override
    public List<ReviewResponseDto> getReviewsByBook(Long bookId) {
        logger.info("Get reviews by book initiated | bookId={}", bookId);

        List<Review> reviews = reviewRepo.findByBookId(bookId);
        if (reviews == null || reviews.isEmpty()) {
            logger.warn("No reviews found for book | bookId={}", bookId);
            throw new ReviewNotFoundException("Review not found for this book");
        }

        List<ReviewResponseDto> response = new ArrayList<>();
        for (Review r : reviews) {
            response.add(new ReviewResponseDto(r.getUser().getName(), r.getComment()));
        }

        logger.info("Get reviews by book successful | bookId={} count={}", bookId, response.size());
        return response;
    }

    @Override
    public List<Review> getReviewsByUser(Long userId) {
        logger.info("Get reviews by user initiated | userId={}", userId);

        List<Review> reviews = reviewRepo.findByUserId(userId);
        logger.info("Get reviews by user successful | userId={} count={}", userId, reviews.size());
        return reviews;
    }
}
//Logging ✅
//→ Unit Testing 🔥
//→ Docker
//→ Redis
//→ Kafka
//→ AWS Hosting
//→ CI/CD
//→ API Gateway + Security (LAST)
