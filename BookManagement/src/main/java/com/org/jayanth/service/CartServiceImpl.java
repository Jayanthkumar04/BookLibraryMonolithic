package com.org.jayanth.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

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

        logger.info("Get user cart initiated | email={}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseGet(() -> {
                    logger.info("No cart found, creating new cart | userId={}", user.getId());
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepo.save(newCart);
                });

        logger.info("Get user cart successful | cartId={}", cart.getId());

        return cart;
    }

    @Override
    public Cart addToCart(String email, Long bookId, int quantity) {

        logger.info("Add to cart initiated | email={} bookId={} quantity={}", email, bookId, quantity);

        Cart cart = getUserCart(email);

        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book Not Found"));

        Optional<CartItem> existing = cartItemRepo.findByCartAndBook(cart, book);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setSubTotal(item.getQuantity() * item.getPrice());
            cartItemRepo.save(item);
            logger.info("Updated existing cart item | cartItemId={} newQuantity={}",
                    item.getId(), item.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setBook(book);
            item.setPrice(book.getPrice());
            item.setQuantity(quantity);
            item.setSubTotal(book.getPrice() * quantity);
            cart.getCartItems().add(item);
            logger.info("Added new cart item | bookId={} quantity={}", bookId, quantity);
        }

        updateTotal(cart);

        Cart savedCart = cartRepo.save(cart);

        logger.info("Add to cart successful | cartId={} totalAmount={}",
                savedCart.getId(), savedCart.getTotalAmount());

        return savedCart;
    }

    @Override
    public Cart updateItem(String email, Long cartItemId, int quantityToAdd) {

        logger.info("Update cart item initiated | email={} cartItemId={} quantityToAdd={}",
                email, cartItemId, quantityToAdd);

        Cart cart = getUserCart(email);

        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart Item Not Found"));

        int newQuantity = item.getQuantity() + quantityToAdd;
        item.setQuantity(newQuantity);
        item.setSubTotal(item.getPrice() * newQuantity);

        cartItemRepo.save(item);

        updateTotal(cart);

        Cart savedCart = cartRepo.save(cart);

        logger.info("Update cart item successful | cartItemId={} newQuantity={} totalAmount={}",
                cartItemId, newQuantity, savedCart.getTotalAmount());

        return savedCart;
    }

    @Override
    public Cart removeItem(String email, Long cartItemId) {

        logger.info("Remove cart item initiated | email={} cartItemId={}", email, cartItemId);

        Cart cart = getUserCart(email);

        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Item not found"));

        cart.getCartItems().remove(item);

        updateTotal(cart);

        Cart savedCart = cartRepo.save(cart);

        logger.info("Remove cart item successful | cartItemId={} totalAmount={}",
                cartItemId, savedCart.getTotalAmount());

        return savedCart;
    }

    @Override
    public Cart clearCart(String email) {

        logger.info("Clear cart initiated | email={}", email);

        Cart cart = getUserCart(email);
        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);

        Cart savedCart = cartRepo.save(cart);

        logger.info("Clear cart successful | cartId={}", savedCart.getId());

        return savedCart;
    }

    @Override
    public void updateTotal(Cart cart) {

        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getSubTotal)
                .sum();

        cart.setTotalAmount(total);

        logger.info("Cart total updated | cartId={} totalAmount={}",
                cart.getId(), total);
    }
}
