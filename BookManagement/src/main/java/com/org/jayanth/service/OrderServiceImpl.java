package com.org.jayanth.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.dtobestprac.OrderResponseDto;
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
import com.org.jayanth.exceptions.CartEmptyException;
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
public class OrderServiceImpl implements OrderService{

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
	
	@Override
	public OrderResponseDto placeOrder(String email, Long addressId) {
	
		User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("USER NOT FOUND"));
		
		Cart cart = cartRepo.findByUser(user).orElseThrow(()->new CartEmptyException("cart is empty"));
		
		if (cart.getCartItems().isEmpty())
            throw new CartEmptyException("Cart is empty");
		
		 Address address = addressRepo.findById(addressId)
	                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

		 String ad = address.getAddressLine1() + ", " + address.getCity()+"\n"+address.getPhone()+"\n"+address.getPostalCode();
		    Order order = new Order();
	        order.setUser(user);
	        order.setOrderItems(new ArrayList<>());
	        order.setShippingAddress(address.getAddressLine1() + ", " + address.getCity()+"\n"+address.getPhone()+"\n"+address.getPostalCode());
            order.setPaymentStatus(PaymentStatus.PAID);
	        double total = 0;
	        
	        String items = "\n";
	        for (CartItem item : cart.getCartItems()) {
	        	
	        	items=items+"\nTitle: "+item.getBook().getTitle()+"\nAuthor: "+item.getBook().getAuthor()+"\nPrice per unit: "+item.getBook().getPrice()+"\nQuantity: "+item.getQuantity();
	        	Book b = item.getBook();
	        	System.out.println(b.getStock());
	        	System.out.println(item.getQuantity());
	        	if(b.getStock() >= item.getQuantity())
	        	{
	        		bookService.updateBookStock(b.getId(), item.getQuantity());
	        	}
	        	else {
	        		
	        		throw new StockNotAvailableException("book stock not available");
	        	}
	            OrderItem oi = new OrderItem();
	            oi.setOrder(order);
	            oi.setBook(item.getBook());
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
	        
	        
	        
	        String subject = "Order Confirmed \n Thanks for placing order \n Your order details are below";
	        
	        String body = "order id: "+order.getId()+"\norder Date:"+order.getOrderDate().getDayOfMonth()+"/"+order.getOrderDate().getMonthValue()+"/"+order.getOrderDate().getYear() +"\nItems: "+items+"\nTotal price"+order.getTotalAmount()+"\nShipping address:"+ad;
	        
	        emailService.orderConfirmation(email,subject ,body );
	        
	        
		return new OrderResponseDto(order.getId(),order.getCreatedAt(),order.getPaymentStatus(),order.getTotalAmount());
	}

	@Override
	public List<Order> getMyOrders(String email) {
		 
		return orderRepo.findByUserEmail(email);
	}

	@Override
	public Order getOrderById(Long orderId, String email) {

		 return orderRepo.findByIdAndUserEmail(orderId, email)
	                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepo.findAll();
	}

	@Override
	public Order updateOrderStatus(Long orderId, OrderStatus status) {
		Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

		if(status.equals(OrderStatus.SHIPPED)) {
        order.setStatus(status);
        order.setShippedAt(LocalDateTime.now());
		}
		else {
			order.setStatus(status);
	        
		}
        return orderRepo.save(order);
        
	}

	@Override
	@Transactional
	public Order cancelOrder(Long orderId, String email) {
	
		Order order = orderRepo.findByIdAndUserEmail(orderId, email)
	            .orElseThrow(() -> new OrderNotFoundException("Order not found"));

		// 1️⃣ Only shipped orders can be cancelled
	    if (order.getStatus() != OrderStatus.SHIPPED) {
	        throw new RuntimeException("Only shipped orders can be cancelled");
	    }
	    
	 // 2️⃣ Check 2-day cancellation window
	    LocalDateTime shippedAt = order.getShippedAt();
	    if (shippedAt == null ||
	        shippedAt.plusDays(2).isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("Cancellation window expired (2 days)");
	    }
	    
	 // 3️⃣ Restore stock
	    for (OrderItem item : order.getOrderItems()) {
	        Book book = item.getBook();
	        book.setStock(book.getStock() + item.getQuantity());
	        bookRepo.save(book);
	    }
	    
	 // 4️⃣ Update order status
	    order.setStatus(OrderStatus.CANCELLED);
	    order.setPaymentStatus(PaymentStatus.REFUNDED);
	    
	    Order saved = orderRepo.save(order);

	    
	    
	    String body = "order cancellation details";
	    // 5️⃣ Optional email
	     emailService.sendOrderCancellation(email,"Order cancellation Confirmation","");

	    return saved;
	}

	
}
