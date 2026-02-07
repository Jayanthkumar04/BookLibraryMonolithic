package com.org.jayanth.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.jayanth.dto.AddressDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Address;
import com.org.jayanth.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

	
	@Autowired
	private AddressService addressService;

	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
	
	 @PostMapping
	    public ResponseEntity<Address> addAddress( @AuthenticationPrincipal(expression = "username") String email, @RequestBody AddressDto dto ) {
		 
		  logger.info("addRequest started {}",email);
	  Address address= addressService.addAddress(email, dto);
			 logger.info("addRequest successfull");
	        return ResponseEntity.status(HttpStatus.CREATED).body(address);
	    }
	 
	 @GetMapping
	    public ResponseEntity<List<Address>> getAddresses(@AuthenticationPrincipal(expression = "username") String email) {
		  
		    logger.info("getAddress request initiated {}",email);
		    List<Address> address = addressService.getUserAddresses(email);
		    logger.info("getAddress request successfull");
		    return ResponseEntity.status(HttpStatus.OK).body(address);
	    }
	 

	 @GetMapping("/{id}")
	    public ResponseEntity<Address> getAddressesById(@AuthenticationPrincipal(expression = "username") String email,@PathVariable Long id) {
		  
		 logger.info("getAddress by id {} started for {}",id,email);
		   Address address = addressService.getUserAddressesById(email,id);
			 logger.info("getAddress by id {} successfull",id);
	        return ResponseEntity.status(HttpStatus.OK).body(address);
	    }
	 
	 @PutMapping("/{id}")
	    public ResponseEntity<Address> update(@PathVariable Long id, 
	                                    @RequestBody AddressDto dto,@AuthenticationPrincipal(expression = "username") String email
	                                  ) {
		 
		    logger.info("update address request initiated for {} ",email );
		    Address address = addressService.updateAddress(email, id, dto);
		    logger.info("update address request successfull");
		   return ResponseEntity.status(HttpStatus.OK).body(address);
	    }
	 
	 @DeleteMapping("/{id}")
	    public ResponseEntity<MessageDto> delete(@AuthenticationPrincipal(expression = "username") String email,@PathVariable Long id) {
		 
		    logger.info("delete address request initiated");		 
		    MessageDto message = addressService.deleteAddress(email, id);
		    logger.info("delete address request successfull");	
	        return ResponseEntity.status(HttpStatus.OK).body(message);
	    }
	 
	 @PutMapping("/{id}/default")
	    public ResponseEntity<Address> setDefault(@AuthenticationPrincipal(expression = "username") String email,@PathVariable Long id) {
		    
		    logger.info("set default address request initiated for {}",email);	
		Address address = addressService.setDefaultAddress(email, id);
	    logger.info("set default address request is successfull");	
	        return ResponseEntity.status(HttpStatus.OK).body(address);
	    }
	
	 
	 
	 
}
