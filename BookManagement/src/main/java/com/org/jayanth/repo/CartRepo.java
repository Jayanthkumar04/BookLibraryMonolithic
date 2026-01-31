package com.org.jayanth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.jayanth.entity.Cart;
import com.org.jayanth.entity.User;

public interface CartRepo extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);
	
	
}
