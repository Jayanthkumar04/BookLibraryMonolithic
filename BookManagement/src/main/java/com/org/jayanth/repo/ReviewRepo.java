package com.org.jayanth.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.jayanth.entity.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long>{

	
	    List<Review> findByBookId(Long bookId);

	    List<Review> findByUserId(Long userId);

	    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);
	
}
