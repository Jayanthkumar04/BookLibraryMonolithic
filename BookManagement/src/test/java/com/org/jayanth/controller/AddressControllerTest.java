package com.org.jayanth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.org.jayanth.service.AddressService;

@WebMvcTest
public class AddressControllerTest {
	
	@MockBean
	private AddressService addressService;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	
	
}
