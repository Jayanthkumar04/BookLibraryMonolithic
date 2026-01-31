package com.org.jayanth.dto;

public class PlaceOrderRequestDto {

	private Long addressId;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	@Override
	public String toString() {
		return "PlaceOrderRequestDto [addressId=" + addressId + "]";
	}
	
}
