package com.org.jayanth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.entity.Book;
import com.org.jayanth.entity.Cart;
import com.org.jayanth.entity.CartItem;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.BookNotFoundException;
import com.org.jayanth.exceptions.CartItemNotFoundException;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.repo.BookRepo;
import com.org.jayanth.repo.CartItemRepo;
import com.org.jayanth.repo.CartRepo;
import com.org.jayanth.repo.UserRepo;

@Service
public class CartServiceImpl implements CartService{

	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private CartItemRepo cartItemRepo;
	
	@Autowired
	private BookRepo bookRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public Cart getUserCart(String email) {
	
		User user = userRepo.findByEmail(email)
							.orElseThrow(()->new UserNotFoundException("user not found"));
	
		 return cartRepo.findByUser(user)
	                .orElseGet(() -> {
	                    Cart cart = new Cart();
	                    cart.setUser(user);
	                    return cartRepo.save(cart);
	                });
		
	}

	@Override
	public Cart addToCart(String email, Long bookId, int quantity) {

		 Cart cart = getUserCart(email);
	        Book book = bookRepo.findById(bookId)
	                .orElseThrow(() -> new BookNotFoundException("Book Not Found"));

	        // Check if item already exists
	        Optional<CartItem> existing = cartItemRepo.findByCartAndBook(cart, book);

	        if (existing.isPresent()) {
	            CartItem item = existing.get();
	            item.setQuantity(item.getQuantity() + quantity);
	            item.setSubTotal(item.getQuantity() * item.getPrice());
	            cartItemRepo.save(item);
	        } else {
	            CartItem item = new CartItem();
	            item.setCart(cart);
	            item.setBook(book);
	            item.setPrice(book.getPrice());
	            item.setQuantity(quantity);
	            item.setSubTotal(book.getPrice() * quantity);

	            cart.getCartItems().add(item);
	        }

	        updateTotal(cart);
	        return cartRepo.save(cart);
	
	}

	@Override
	public Cart updateItem(String email, Long cartItemId, int quantityToAdd) {
		Cart cart = getUserCart(email);

        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart Item Not Found"));

        int newQuantity = item.getQuantity()+quantityToAdd;
        item.setQuantity(newQuantity);
        item.setSubTotal(item.getPrice() * newQuantity);
        cartItemRepo.save(item);

        updateTotal(cart);
        return cartRepo.save(cart);
	}

	@Override
	public Cart removeItem(String email, Long cartItemId) {
	
		Cart cart = getUserCart(email);

		System.out.println(cart);

		CartItem item = cartItemRepo.findById(cartItemId)
								.orElseThrow(()-> new CartItemNotFoundException("Item not found"));
		System.out.println(item);

		cart.getCartItems().remove(item);
		
		
		updateTotal(cart);
		
		return cartRepo.save(cart);
	}

	@Override
	public Cart clearCart(String email) {
		Cart cart = getUserCart(email);
        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);
        return cartRepo.save(cart);
	}

	@Override
	public void updateTotal(Cart cart) {
	
		 double total = cart.getCartItems().stream()
	                .mapToDouble(CartItem::getSubTotal)
	                .sum();
	        cart.setTotalAmount(total);
	}

	
}
