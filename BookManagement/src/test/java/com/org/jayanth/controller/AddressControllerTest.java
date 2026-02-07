package com.org.jayanth.controller;

import static org.hamcrest.CoreMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.org.jayanth.entity.Address;
import com.org.jayanth.security.FirstLoginFilter;
import com.org.jayanth.service.AddressService;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters  = false)
@ImportAutoConfiguration(exclude = {
    SecurityAutoConfiguration.class,
    SecurityFilterAutoConfiguration.class
})
class AddressControllerTest {

	
	@MockBean
	private AddressService addressService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void testAddAddress()
	{
		

		
		
	}
	
	@Test
	void testGetAllAddress() throws Exception
	{
		
		Address address1 = new Address();
		
		Address address2 = new Address();
		
		List<Address> addresses = new ArrayList<>();
		
		addresses.add(address1); addresses.add(address2);
	

		when(addressService.getUserAddresses(anyString()))
        .thenReturn(addresses);
		
		MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/api/addresses");
		
		MvcResult result = mockMvc.perform(req).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		int status =response.getStatus();
		
		assertEquals(200,status);
	}
	
	@Test
	void testGetAddressById()
	{
		
	}
	
	@Test
	void testUpdateAddress()
	{
		
	}
	
	@Test
	void testDeleteAddress()
	{
		
	}
	
	@Test
	void testUpdateDefaultAddress()
	{
		
	}
	
	
	
}
