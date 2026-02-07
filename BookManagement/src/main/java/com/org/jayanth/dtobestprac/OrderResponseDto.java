package com.org.jayanth.dtobestprac;
import java.time.LocalDateTime;
import com.org.jayanth.entity.PaymentStatus;
public class OrderResponseDto {

	private Long id;
	
	private LocalDateTime createdAt;
	
	private PaymentStatus paymentStatus;
	
	private Double totalAmount;

	public OrderResponseDto() {
		super();
		}

	public OrderResponseDto(Long id, LocalDateTime createdAt, PaymentStatus paymentStatus, Double totalAmount) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.paymentStatus = paymentStatus;
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "OrderDto [id=" + id + ", createdAt=" + createdAt + ", paymentStatus=" + paymentStatus + ", totalAmount="
				+ totalAmount + "]";
	}
	
	
}
