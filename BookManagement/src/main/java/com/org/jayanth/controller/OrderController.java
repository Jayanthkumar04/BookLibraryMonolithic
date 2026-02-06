package com.org.jayanth.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.org.jayanth.dto.PlaceOrderRequestDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Order;
import com.org.jayanth.entity.OrderStatus;
import com.org.jayanth.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    private String getEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // ---------------- PLACE ORDER ----------------
    @PostMapping("/place")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MessageDto> placeOrder(
            @RequestBody PlaceOrderRequestDto request) {

        String email = getEmail();

        logger.info(
                "Place order request initiated | user={} addressId={}",
                email, request.getAddressId()
        );

        MessageDto response = orderService.placeOrder(email, request.getAddressId());

        logger.info(
                "Place order request successful | user={}",
                email
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------- GET MY ORDERS ----------------
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Order>> getMyOrders() {

        String email = getEmail();

        logger.info(
                "Get my orders request initiated | user={}",
                email
        );

        List<Order> orders = orderService.getMyOrders(email);

        logger.info(
                "Get my orders request successful | user={} totalOrders={}",
                email, orders.size()
        );

        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    // ---------------- GET ORDER BY ID ----------------
    @GetMapping("/id/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId) {

        logger.info(
                "Get order by id request initiated | orderId={} user={}",
                orderId, getEmail()
        );

        Order order = orderService.getOrderById(orderId, getEmail());

        logger.info(
                "Get order by id request successful | orderId={}",
                orderId
        );

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    // ---------------- ADMIN: GET ALL ORDERS ----------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {

        logger.info("Admin request - get all orders initiated");

        List<Order> orders = orderService.getAllOrders();

        logger.info(
                "Admin request - get all orders successful | totalOrders={}",
                orders.size()
        );

        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    // ---------------- ADMIN: UPDATE ORDER STATUS ----------------
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        logger.info(
                "Admin request - update order status initiated | orderId={} status={}",
                orderId, status
        );

        Order order = orderService.updateOrderStatus(orderId, status);

        logger.info(
                "Admin request - update order status successful | orderId={} status={}",
                orderId, status
        );

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    // ---------------- CANCEL ORDER ----------------
    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {

        String email = getEmail();

        logger.info(
                "Cancel order request initiated | orderId={} user={}",
                orderId, email
        );

        Order cancelledOrder = orderService.cancelOrder(orderId, email);

        logger.info(
                "Cancel order request successful | orderId={} user={}",
                orderId, email
        );

        return ResponseEntity.status(HttpStatus.OK).body(cancelledOrder);
    }
}
