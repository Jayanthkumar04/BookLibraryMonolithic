package com.org.jayanth.dtobestprac;

public class CartItemDto {

    private Long cartItemId;
    private Long bookId;
    private String bookTitle;
    private Double price;
    private Integer quantity;
    private Double subTotal;
	public CartItemDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CartItemDto(Long cartItemId, Long bookId, String bookTitle, Double price, Integer quantity,
			Double subTotal) {
		super();
		this.cartItemId = cartItemId;
		this.bookId = bookId;
		this.bookTitle = bookTitle;
		this.price = price;
		this.quantity = quantity;
		this.subTotal = subTotal;
	}
	public Long getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
    
    
}
