package com.org.jayanth.dtobestprac;

public class ReviewResponseDto {

	private String customerName;
	private String comments;
	public ReviewResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReviewResponseDto(String customerName, String comments) {
		super();
		this.customerName = customerName;
		this.comments = comments;
	}
	
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "ReviewResponseDto [customerName=" + customerName + ", comments=" + comments + "]";
	}
	
	
}
