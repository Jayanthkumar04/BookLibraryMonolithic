package com.org.jayanth.service;

import java.util.List;

import com.org.jayanth.dto.AddressDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Address;

public interface AddressService {

	Address addAddress(String email,AddressDto dto);
	
	List<Address> getUserAddresses(String email);
	
	Address updateAddress(String email,Long addressId,AddressDto dto);
	
	MessageDto deleteAddress(String email,Long addressId);
	
	Address setDefaultAddress(String email,Long addressId);

	Address getUserAddressesById(String email, Long id);
	
	
}
