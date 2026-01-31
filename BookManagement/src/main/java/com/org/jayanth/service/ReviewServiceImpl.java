package com.org.jayanth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.ReviewDto;
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
public class ReviewServiceImpl implements ReviewService
{
	
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
	
		User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
		Book book = bookRepo.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book Not Found"));

		
		
		if (!orderItemRepo.hasUserPurchasedBook(user.getId(), dto.getBookId())) {
		    throw new UserUnauthorizedPageException("You can only review books you purchased!");
		}
		
		Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(dto.getRating());
        review.setComment(dto.getComments());
		System.out.println(dto.getComments());
		return reviewRepo.save(review);
	}

	@Override
	public Review updateReview(String email, Long reviewId, ReviewDto dto)
	{
	User user = userRepo.findByEmail(email)
    .orElseThrow(() -> new UserNotFoundException("User Not Found"));
 
	 Review review = reviewRepo.findById(reviewId)
             .orElseThrow(() -> new ReviewNotFoundException("Review Not Found"));

	 if (!review.getUser().getId().equals(user.getId())) {
         throw new UserUnauthorizedPageException("You cannot edit someone else’s review");
     }
	 
	if(dto.getRating() != null)
	 review.setRating(dto.getRating());
	if(dto.getComments() != null)
     review.setComment(dto.getComments());

     return reviewRepo.save(review);
	
	}

	@Override
	public void deleteReview(String email, Long reviewId) {
		
		  User user = userRepo.findByEmail(email)
	                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

	        Review review = reviewRepo.findById(reviewId)
	                .orElseThrow(() -> new ReviewNotFoundException("Review Not Found"));

	        if (!review.getUser().getId().equals(user.getId())) {
	            throw new UserUnauthorizedPageException("You cannot delete someone else’s review");
	        }

	        reviewRepo.delete(review);
	}

	@Override
	public List<ReviewResponseDto> getReviewsByBook(Long bookId) {
		
		List<Review>  r = reviewRepo.findByBookId(bookId);
		
		if(r == null) throw new ReviewNotFoundException("review not found for this book");
		
		List<ReviewResponseDto> all = new ArrayList<>();
		for(Review responses:r)
		{
			all.add(new ReviewResponseDto(responses.getUser().getName(),responses.getComment()));
			
		}
		
		return all;
	}

	@Override
	public List<Review> getReviewsByUser(Long userId) {
		
		List<Review> reviews = reviewRepo.findByUserId(userId);
		
		return reviews;
	}
	
}
