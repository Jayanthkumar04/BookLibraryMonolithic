package com.org.jayanth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.org.jayanth.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem,Long>{
	
	@Query("SELECT COUNT(oi) > 0 FROM OrderItem oi WHERE oi.order.user.id = :userId AND oi.book.id = :bookId")
    boolean hasUserPurchasedBook(Long userId, Long bookId);
	
}
