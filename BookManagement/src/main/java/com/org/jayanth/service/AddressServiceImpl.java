package com.org.jayanth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.AddressDto;
import com.org.jayanth.entity.Address;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.AddressNotFoundException;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.exceptions.UserUnauthorizedPageException;
import com.org.jayanth.repo.AddressRepo;
import com.org.jayanth.repo.UserRepo;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private AddressRepo addressRepo;
	
	
	
	
	
	@Override
	public Address addAddress(String email, AddressDto dto) {
	
		User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found"));
		
		Address address = new Address();
		
		address.setUser(user);
        address.setName(dto.getName());
        address.setPhone(dto.getPhone());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setAddressType(dto.getAddressType());
        
        // If this is default address → remove default from previous ones

        if(dto.isDefault())
        {
        	clearDefaultAddress(user.getId());
        	
        	address.setDefault(true);
        }
        
        return addressRepo.save(address);
	
	}

	private void clearDefaultAddress(Long id) {
		
		List<Address> addresses = addressRepo.findByUserId(id);
		
		for(Address a:addresses)
		{
			a.setDefault(false);
			addressRepo.save(a);
		}
		
	}

	@Override
	public List<Address> getUserAddresses(String email) {
		
		User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("user not found"));
		return addressRepo.findByUserId(user.getId());
	}

	@Override
	public Address updateAddress(String email, Long addressId, AddressDto dto) {
		User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

     // prevent other users from modifying someone else's address
        if (!address.getUser().getId().equals(user.getId())) {
            throw new UserUnauthorizedPageException("Unauthorized");
        
        }
        
        address.setName(dto.getName());
        address.setPhone(dto.getPhone());
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setAddressType(dto.getAddressType());
        
        if (dto.isDefault()) {
            clearDefaultAddress(user.getId());
            address.setDefault(true);
        }
        
        return addressRepo.save(address);
        
	}

	@Override
	public void deleteAddress(String email, Long addressId) {
		User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new UserUnauthorizedPageException("Unauthorized");
        }

        addressRepo.delete(address);
	}

	@Override
	public Address setDefaultAddress(String email, Long addressId) {
		User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new UserUnauthorizedPageException("Unauthorized");
        }

        clearDefaultAddress(user.getId());
        address.setDefault(true);

        return addressRepo.save(address);
	}

	@Override
	public Address getUserAddressesById(String email, Long id) {
		
		User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new UserUnauthorizedPageException("Unauthorized");
        }
        
        

		return address;
	}

	
}
