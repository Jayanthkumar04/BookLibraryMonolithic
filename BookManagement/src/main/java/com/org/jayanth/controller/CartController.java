package com.org.jayanth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.jayanth.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	private String getEmail()
	{
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();
	}
	@GetMapping
    public ResponseEntity<?> getCart() {
        return ResponseEntity.ok(cartService.getUserCart(getEmail()));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long bookId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.addToCart(getEmail(), bookId, quantity));
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<?> updateItem(
            @PathVariable Long itemId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(cartService.updateItem(getEmail(), itemId, quantity));
    }

    @DeleteMapping("/remove/item/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId) {
    	System.out.println("request received");
        return ResponseEntity.ok(cartService.removeItem(getEmail(), itemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        return ResponseEntity.ok(cartService.clearCart(getEmail()));
    }
}