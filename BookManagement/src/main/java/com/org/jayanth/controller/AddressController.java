package com.org.jayanth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.jayanth.dto.AddressDto;
import com.org.jayanth.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

	
	@Autowired
	private AddressService addressService;
	
	/*@AuthenticationPrincipal (correct spelling) is a Spring Security annotation that allows 
	 * you to directly inject
	 *  the currently logged-in user (or the principal object) into a controller method.
	 * 
	 * When a user logs in using JWT or normal Spring Security, Spring stores a Principal object in the SecurityContext.

Instead of manually extracting email like this:
	 * 
	 * String email = SecurityContextHolder.getContext().getAuthentication().getName();

	 * 
	 */
	
	private String getEmail()
	{
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();
	}
	
	 @PostMapping
	    public ResponseEntity<?> addAddress(@RequestBody AddressDto dto ) {
		 
		   String email = getEmail();
		 
	        return ResponseEntity.ok(addressService.addAddress(email, dto));
	    }
	 
	 @GetMapping
	    public ResponseEntity<?> getAddresses() {
		  
		    String email = getEmail();
		    
	        return ResponseEntity.ok(addressService.getUserAddresses(email));
	    }
	 

	 @GetMapping("/{id}")
	    public ResponseEntity<?> getAddresses(@PathVariable Long id) {
		  
		    String email = getEmail();
		    
	        return ResponseEntity.ok(addressService.getUserAddressesById(email,id));
	    }
	 
	 @PutMapping("/{id}")
	    public ResponseEntity<?> update(@PathVariable Long id, 
	                                    @RequestBody AddressDto dto
	                                  ) {
		    String email = getEmail();
	        return ResponseEntity.ok(addressService.updateAddress(email, id, dto));
	    }
	 
	 @DeleteMapping("/{id}")
	    public ResponseEntity<?> delete(@PathVariable Long id) {
		 
		 String email = getEmail();
	        addressService.deleteAddress(email, id);
	        return ResponseEntity.noContent().build();
	    }
	 
	 @PutMapping("/{id}/default")
	    public ResponseEntity<?> setDefault(@PathVariable Long id) {
		    
		 String email = getEmail();
	        return ResponseEntity.ok(addressService.setDefaultAddress(email, id));
	    }
	
	 
	 
	 
}
