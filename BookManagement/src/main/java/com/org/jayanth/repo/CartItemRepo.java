package com.org.jayanth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.jayanth.entity.Book;
import com.org.jayanth.entity.Cart;
import com.org.jayanth.entity.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Long>{

	Optional<CartItem> findByCartAndBook(Cart cart,Book book);
	
}
