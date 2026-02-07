package com.org.jayanth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cart_items")
public class CartItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
    
	@ManyToOne
	@JoinColumn(name="cart_id")
	@JsonIgnore
	private Cart cart;

	@ManyToOne
	@JoinColumn(name="book_id")
	@JsonIgnore
	private Book book;
	
	
	private Integer quantity;
	
	private Double price;
	
	private Double subTotal;

	public CartItem() {
		super();
	}

	public CartItem(Long id, Cart cart, Book book, Integer quantity, Double price, Double subTotal) {
		super();
		this.id = id;
		this.cart = cart;
		this.book = book;
		this.quantity = quantity;
		this.price = price;
		this.subTotal = subTotal;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", cart=" + cart + ", book=" + book + ", quantity=" + quantity + ", price="
				+ price + ", subTotal=" + subTotal + "]";
	}
	
	
	
	
}
