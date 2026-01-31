package com.org.jayanth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.jayanth.dto.PlaceOrderRequestDto;
import com.org.jayanth.dtobestprac.OrderResponseDto;
import com.org.jayanth.entity.Order;
import com.org.jayanth.entity.OrderStatus;
import com.org.jayanth.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	
	@Autowired
	private OrderService orderService;
	
	public String getEmail()
	{
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();
		
	}
	

	 @PostMapping("/place")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public  ResponseEntity<String> placeOrder(
	            @RequestBody PlaceOrderRequestDto request) {
               
		 String email = getEmail();
	        OrderResponseDto order = orderService.placeOrder(email, request.getAddressId());
//	        return ResponseEntity.ok(order);
	       return ResponseEntity.ok("order placed successfully");  
	 }
	 
	 @GetMapping("/my")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<?> getMyOrders() {
		 String email = getEmail();
	        return ResponseEntity.ok(orderService.getMyOrders(email));
	    }
	 
	 @GetMapping("/id/{orderId}")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<?> getOrderById(
	            @PathVariable Long orderId) {

	        return ResponseEntity.ok(orderService.getOrderById(orderId, getEmail()));
	    }
	
	// ---------------- ADMIN ----------------

	    @GetMapping
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> getAllOrders() {
	        return ResponseEntity.ok(orderService.getAllOrders());
	    }

	    @PutMapping("/{orderId}/status")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> updateStatus(
	            @PathVariable Long orderId,
	            @RequestParam OrderStatus status) {

	        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
	    }
	
	    @PutMapping("/{orderId}/cancel")
	    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {

	        String email = getEmail();
	        
	        Order cancelledOrder = orderService.cancelOrder(orderId, email);
	        
	        return ResponseEntity.ok(cancelledOrder);
	    }
	    
}
