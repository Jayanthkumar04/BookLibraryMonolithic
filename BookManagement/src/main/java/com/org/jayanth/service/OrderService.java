package com.org.jayanth.service;

import java.util.List;

import com.org.jayanth.dtobestprac.OrderResponseDto;
import com.org.jayanth.entity.Order;
import com.org.jayanth.entity.OrderStatus;

public interface OrderService {

	OrderResponseDto placeOrder(String email,Long addressId);
	
	List<Order> getMyOrders(String email);
	
	Order getOrderById(Long orderId,String email);
	
	List<Order> getAllOrders();
	
	Order updateOrderStatus(Long orderId,OrderStatus status);
	
	Order cancelOrder(Long orderId,String email);
}
