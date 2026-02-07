package com.org.jayanth.service;

import com.org.jayanth.entity.Cart;

public interface CartService {

	Cart getUserCart(String email);
	
	Cart addToCart(String email,Long bookId,int quantity);

	Cart updateItem(String email,Long cartItemId,int quantity);
	
	Cart removeItem(String email,Long itemId);
	
	Cart clearCart(String email);
	
	void updateTotal(Cart cart);
	
}
