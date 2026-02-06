package com.org.jayanth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.org.jayanth.entity.Cart;
import com.org.jayanth.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    private String getEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // ---------------------- GET CART ----------------------
    @GetMapping
    public ResponseEntity<Cart> getCart() {

        logger.info("Get cart request initiated for user {}", getEmail());

        Cart cart = cartService.getUserCart(getEmail());

        logger.info("Get cart request successful for user {}", getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    // ---------------------- ADD TO CART ----------------------
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @RequestParam Long bookId,
            @RequestParam int quantity
    ) {

        logger.info(
                "Add to cart request initiated | user={} bookId={} quantity={}",
                getEmail(), bookId, quantity
        );

        Cart cart = cartService.addToCart(getEmail(), bookId, quantity);

        logger.info(
                "Add to cart request successful | user={} bookId={}",
                getEmail(), bookId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    // ---------------------- UPDATE CART ITEM ----------------------
    @PutMapping("/update/{itemId}")
    public ResponseEntity<Cart> updateItem(
            @PathVariable Long itemId,
            @RequestParam int quantity
    ) {

        logger.info(
                "Update cart item request initiated | user={} itemId={} quantity={}",
                getEmail(), itemId, quantity
        );

        Cart cart = cartService.updateItem(getEmail(), itemId, quantity);

        logger.info(
                "Update cart item request successful | user={} itemId={}",
                getEmail(), itemId
        );

        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    // ---------------------- REMOVE ITEM ----------------------
    @DeleteMapping("/remove/item/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long itemId) {

        logger.info(
                "Remove cart item request initiated | user={} itemId={}",
                getEmail(), itemId
        );

        Cart cart = cartService.removeItem(getEmail(), itemId);

        logger.info(
                "Remove cart item request successful | user={} itemId={}",
                getEmail(), itemId
        );

        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    // ---------------------- CLEAR CART ----------------------
    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clearCart() {

        logger.info("Clear cart request initiated for user {}", getEmail());

        Cart cart = cartService.clearCart(getEmail());

        logger.info("Clear cart request successful for user {}", getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }
}
