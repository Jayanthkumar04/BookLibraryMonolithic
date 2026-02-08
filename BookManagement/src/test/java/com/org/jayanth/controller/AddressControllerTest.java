package com.org.jayanth.controller;


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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.org.jayanth.dto.AddressDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Address;
import com.org.jayanth.entity.AddressType;
import com.org.jayanth.security.FirstLoginFilter;
import com.org.jayanth.security.JwtAuthenticationFilter;
import com.org.jayanth.service.AddressService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.AssertTrue;
@WebMvcTest(
	    controllers = AddressController.class,
	    excludeFilters = {
	        @ComponentScan.Filter(
	            type = FilterType.ASSIGNABLE_TYPE,
	            classes = {
	                FirstLoginFilter.class,
	                JwtAuthenticationFilter.class
	            }
	        )
	    }
	)
	@AutoConfigureMockMvc(addFilters = false)
	class AddressControllerTest {

	    @MockBean
	    private AddressService addressService;

	    @Autowired
	    private MockMvc mockMvc;

	    @Test
	    void testGetAllAddress() throws Exception {

	        List<Address> addresses = List.of(new Address(), new Address());

	        when(addressService.getUserAddresses(anyString()))
	                .thenReturn(addresses);

	        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get("/api/addresses"); 
	        MvcResult result = mockMvc.perform(req).andReturn(); 
	        MockHttpServletResponse response = result.getResponse(); 
	        int status =response.getStatus(); 
	        assertEquals(200,status);     
	        
	    }
	    
	    @Test
	    void testGetAddressById() throws Exception
	    {
	    	Address address = new Address();
	    	
	    	address.setId(1L);
	    	address.setName("Jayanth");
	    	address.setPhone("9876543210");
	    	address.setAddressLine1("Flat 301, Green Residency");
	    	address.setAddressLine2("Near Metro Station");
	    	address.setCity("Hyderabad");
	    	address.setState("Telangana");
	    	address.setCountry("India");
	    	address.setPostalCode("500081");
	    	address.setAddressType(AddressType.HOME);
	    	address.setDefault(true);
	    	
	    	when(addressService.getUserAddressesById(any(), anyLong())).thenReturn(address);
	    	
	    	Long addressId = 1L;
	    	MockHttpServletRequestBuilder req =  MockMvcRequestBuilders.get("/api/addresses/{id}",addressId);
	    	
	    	MvcResult result = mockMvc.perform(req).andReturn();
	    	
	    	MockHttpServletResponse response = result.getResponse();
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	int status = response.getStatus();
	    	String responseContent = response.getContentAsString();
	    	String addressJson = mapper.writeValueAsString(address);
	    	assertEquals(200, status);
	    	assertEquals(addressJson, responseContent);
	    	
	    	
	    	
	    }
	    
	    @Test
	    void testAddAddress() throws Exception
	    {
	    	Address address = new Address();
	    	
	    	address.setId(1L);
	    	address.setName("Jayanth");
	    	address.setPhone("9876543210");
	    	address.setAddressLine1("Flat 301, Green Residency");
	    	address.setAddressLine2("Near Metro Station");
	    	address.setCity("Hyderabad");
	    	address.setState("Telangana");
	    	address.setCountry("India");
	    	address.setPostalCode("500081");
	    	address.setAddressType(AddressType.HOME);
	    	address.setDefault(true);

	    	when(addressService.addAddress(any(),any(AddressDto.class))).thenReturn(address);
	    	
	    	ObjectMapper mapper = new ObjectMapper();
	    	
	    	String addressJsonStr = mapper.writeValueAsString(address);
	    	
	    	MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post("/api/addresses").contentType("application/json")
	    																		  .content(addressJsonStr);
	    	
	    	MvcResult result = mockMvc.perform(req).andReturn();
	    	
	    	MockHttpServletResponse response = result.getResponse();
	    	
	    	assertEquals(201, response.getStatus());
	    	
	    	assertEquals(response.getContentAsString(),addressJsonStr);
	    	
	    }
	    
	    @Test
	    void testUpdateAddress() throws Exception {

	        // given
	        AddressDto dto = new AddressDto();
	        dto.setName("Jayanth Updated");
	        dto.setPhone("9123456780");
	        dto.setAddressLine1("Flat 401, Blue Residency");
	        dto.setCity("Hyderabad");
	        dto.setState("Telangana");
	        dto.setPostalCode("500081");
	        dto.setAddressType(AddressType.HOME);
	        dto.setDefault(true);

	        Address updatedAddress = new Address();
	        updatedAddress.setId(1L);
	        updatedAddress.setName(dto.getName());
	        updatedAddress.setPhone(dto.getPhone());
	        updatedAddress.setAddressLine1(dto.getAddressLine1());
	        updatedAddress.setCity(dto.getCity());
	        updatedAddress.setState(dto.getState());
	        updatedAddress.setPostalCode(dto.getPostalCode());
	        updatedAddress.setAddressType(dto.getAddressType());
	        updatedAddress.setDefault(dto.isDefault());

	        // Mock the service - use typed matchers for all arguments
	        when(addressService.updateAddress(any(), eq(1L), any(AddressDto.class)))
	                .thenReturn(updatedAddress);

	        ObjectMapper mapper = new ObjectMapper();
	        String dtoJson = mapper.writeValueAsString(dto);

	        // when + then
	        mockMvc.perform(
	                MockMvcRequestBuilders.put("/api/addresses/{id}", 1L)
	                        .contentType("application/json")
	                        .content(dtoJson)
	        )
	        .andExpect(status().isOk());
	 

	        // Verify service method called - use matchers for all arguments
	        verify(addressService).updateAddress(any(), eq(1L), any(AddressDto.class));
	    }

 
	    @Test
	    void testDeleteAddress() throws Exception {

	        // given
	        MessageDto message = new MessageDto("Address deleted successfully");
	     

	        // email will be null → use any() or isNull()
	        when(addressService.deleteAddress(any(), eq(1L)))
	                .thenReturn(message);

	        ObjectMapper mapper = new ObjectMapper();
	        
	        String messageStr = mapper.writeValueAsString(message);
	        
	        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.delete("/api/addresses/{id}",1L);
	        
	        MvcResult result = mockMvc.perform(req).andReturn();
	        
	        MockHttpServletResponse response = result.getResponse();
	        

	        assertEquals(200, response.getStatus());
	        
	     
}

	    
	    @Test
	    void testSetDefaultAddress() throws Exception {

	        // given
	        Address address = new Address();
	        address.setId(1L);
	        address.setName("Jayanth");
	        address.setCity("Hyderabad");
	        address.setDefault(true);
	        
	        ObjectMapper mapper = new ObjectMapper();
	        
	        String addStr = mapper.writeValueAsString(address);

	        // email will be null in @WebMvcTest
	        when(addressService.setDefaultAddress(any(), eq(1L))).thenReturn(address);
	    
	        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.put("/api/addresses/{id}/default",1L);
	        
	        MvcResult result = mockMvc.perform(req).andReturn();
	        
	        MockHttpServletResponse response = result.getResponse();
	        
	        
	        assertEquals(200, response.getStatus());
	        
	        assertEquals(addStr, response.getContentAsString());
	        

	       
	    }

	     
	    

}
