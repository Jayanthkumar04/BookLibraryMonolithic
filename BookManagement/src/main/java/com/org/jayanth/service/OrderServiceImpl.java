package com.org.jayanth.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Address;
import com.org.jayanth.entity.Book;
import com.org.jayanth.entity.Cart;
import com.org.jayanth.entity.CartItem;
import com.org.jayanth.entity.Order;
import com.org.jayanth.entity.OrderItem;
import com.org.jayanth.entity.OrderStatus;
import com.org.jayanth.entity.PaymentStatus;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.AddressNotFoundException;
import com.org.jayanth.exceptions.CancellationWindowExpiredException;
import com.org.jayanth.exceptions.CartEmptyException;
import com.org.jayanth.exceptions.InvalidOrderStateException;
import com.org.jayanth.exceptions.OrderNotFoundException;
import com.org.jayanth.exceptions.StockNotAvailableException;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.repo.AddressRepo;
import com.org.jayanth.repo.BookRepo;
import com.org.jayanth.repo.CartItemRepo;
import com.org.jayanth.repo.CartRepo;
import com.org.jayanth.repo.OrderRepo;
import com.org.jayanth.repo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepo bookRepo;
    
    private static final String ORDER_NOT_FOUND  = "order not found";

    @Override
    public MessageDto placeOrder(String email, Long addressId) {

        logger.info("Place order initiated | email={} addressId={}", email, addressId);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found | email={}", email);
                    return new UserNotFoundException("USER NOT FOUND");
                });

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("Cart empty for user | email={}", email);
                    return new CartEmptyException("cart is empty");
                });

        if (cart.getCartItems().isEmpty()) {
            logger.error("Cart is empty for user | email={}", email);
            throw new CartEmptyException("Cart is empty");
        }

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> {
                    logger.error("Address not found | addressId={}", addressId);
                    return new AddressNotFoundException("Address not found");
                });

        if (!address.getUser().getId().equals(user.getId())) {
            logger.error("Address does not belong to user | email={} addressId={}", email, addressId);
            throw new AddressNotFoundException("Wrong Address, Please add correct address");
        }

        logger.info("Address verified for order | email={} addressId={}", email, addressId);

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(new ArrayList<>());
        order.setShippingAddress(address.getAddressLine1() + ", " + address.getCity() + "\n" + address.getPhone() + "\n" + address.getPostalCode());
        order.setPaymentStatus(PaymentStatus.PAID);

        double total = 0;
        StringBuilder items = new StringBuilder();

        for (CartItem item : cart.getCartItems()) {

            Book b = item.getBook();
            logger.info("Processing cart item | bookId={} title={} quantity={} stock={}", b.getId(), b.getTitle(), item.getQuantity(), b.getStock());

            if (b.getStock() < item.getQuantity()) {
                logger.error("Stock not available | bookId={} required={} available={}", b.getId(), item.getQuantity(), b.getStock());
                throw new StockNotAvailableException("book stock not available");
            }

            bookService.updateBookStock(b.getId(), item.getQuantity());

            items.append("\n--------------------")
            .append("\nTitle: ").append(b.getTitle())
            .append("\nAuthor: ").append(b.getAuthor())
            .append("\nPrice per unit: ").append(b.getPrice())
            .append("\nQuantity: ").append(item.getQuantity());

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setBook(b);
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getPrice());
            oi.setSubtotal(item.getSubTotal());

            total += item.getSubTotal();

            order.getOrderItems().add(oi);
        }

        order.setTotalAmount(total);
        Order saved = orderRepo.save(order);

        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);
        cartRepo.save(cart);

        logger.info("Order placed successfully | orderId={} totalAmount={}", saved.getId(), total);

        String subject = "Order Confirmed \n Thanks for placing order \n Your order details are below";
        String body = "order id: " + order.getId() + "\norder Date:" + order.getOrderDate() + "\nItems: " + items.toString() + "\nTotal price" + order.getTotalAmount();

        emailService.sendEmail(email, subject, body);
        logger.info("Order confirmation email sent | email={} orderId={}", email, saved.getId());

        return new MessageDto("order has been placed successfully");
    }

    @Override
    public List<Order> getMyOrders(String email) {
        logger.info("Get my orders initiated | email={}", email);
        List<Order> orders = orderRepo.findByUserEmail(email);
        logger.info("Get my orders successful | email={} count={}", email, orders.size());
        return orders;
    }

    @Override
    public Order getOrderById(Long orderId, String email) {
        logger.info("Get order by id initiated | orderId={} email={}", orderId, email);
        Order order = orderRepo.findByIdAndUserEmail(orderId, email)
                .orElseThrow(() -> {
                    logger.error("Order not found | orderId={} email={}", orderId, email);
                    return new OrderNotFoundException(ORDER_NOT_FOUND);
                });
        logger.info("Get order by id successful | orderId={} email={}", orderId, email);
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        logger.info("Get all orders initiated");
        List<Order> orders = orderRepo.findAll();
        logger.info("Get all orders successful | count={}", orders.size());
        return orders;
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        logger.info("Update order status initiated | orderId={} status={}", orderId, status);

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found for status update | orderId={}", orderId);
                    return new OrderNotFoundException(ORDER_NOT_FOUND);
                });

        if (status.equals(OrderStatus.SHIPPED)) {
            order.setStatus(status);
            order.setShippedAt(LocalDateTime.now());
        } else {
            order.setStatus(status);
        }

        Order saved = orderRepo.save(order);
        logger.info("Order status updated | orderId={} newStatus={}", orderId, status);
        return saved;
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId, String email) {

        logger.info("Cancel order initiated | orderId={} email={}", orderId, email);

        Order order = orderRepo.findByIdAndUserEmail(orderId, email)
                .orElseThrow(() -> {
                    logger.error("Order not found for cancellation | orderId={} email={}", orderId, email);
                    return new OrderNotFoundException(ORDER_NOT_FOUND);
                });

       
        if (order.getStatus() != OrderStatus.SHIPPED){
            logger.error("Cannot cancel order, not shipped | orderId={} status={}", orderId, order.getStatus());
            throw new InvalidOrderStateException("Only shipped orders can be cancelled");
        }

        LocalDateTime shippedAt = order.getShippedAt();
        if (shippedAt == null || shippedAt.plusDays(2).isBefore(LocalDateTime.now())) {
            logger.error("Cancellation window expired | orderId={} shippedAt={}", orderId, shippedAt);
            throw new CancellationWindowExpiredException("Cancellation window expired (2 days)");
        }

        for (OrderItem item : order.getOrderItems()) {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
            bookRepo.save(book);
            logger.info("Restored book stock | bookId={} quantityRestored={}", book.getId(), item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);

        Order saved = orderRepo.save(order);

        emailService.sendEmail(email, "Order cancellation Confirmation", "");
        logger.info("Order cancelled successfully and email sent | orderId={} email={}", orderId, email);

        return saved;
    }
}
