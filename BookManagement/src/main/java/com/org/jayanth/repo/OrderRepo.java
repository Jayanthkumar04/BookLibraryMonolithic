package com.org.jayanth.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.jayanth.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {

	List<Order> findByUserEmail(String email);

	Optional<Order> findByIdAndUserEmail(Long orderId, String email);

}
