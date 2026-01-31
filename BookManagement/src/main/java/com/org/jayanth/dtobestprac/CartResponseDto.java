package com.org.jayanth.dtobestprac;

import java.util.List;

public class CartResponseDto {

    private Long cartId;
    private Double totalAmount;
    private List<CartItemDto> items;
	public CartResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartResponseDto(Long cartId, Double totalAmount, List<CartItemDto> items) {
		super();
		this.cartId = cartId;
		this.totalAmount = totalAmount;
		this.items = items;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<CartItemDto> getItems() {
		return items;
	}
	public void setItems(List<CartItemDto> items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "CartResponseDto [cartId=" + cartId + ", totalAmount=" + totalAmount + ", items=" + items + "]";
	}
    
    
}

