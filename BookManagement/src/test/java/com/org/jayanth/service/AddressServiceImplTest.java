package com.org.jayanth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.org.jayanth.dto.AddressDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Address;
import com.org.jayanth.entity.AddressType;
import com.org.jayanth.entity.User;
import com.org.jayanth.repo.AddressRepo;
import com.org.jayanth.repo.UserRepo;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private AddressServiceImpl service;

    @Test
    void testAddAddress() {

        AddressDto dto = new AddressDto();
        dto.setName("Jayanth");
        dto.setPhone("9876543210");
        dto.setAddressLine1("Flat 301");
        dto.setCity("Hyderabad");
        dto.setState("Telangana");
        dto.setPostalCode("500081");
        dto.setAddressType(AddressType.HOME);
        dto.setDefault(true);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        Address savedAddress = new Address();
        savedAddress.setId(1L);
        savedAddress.setUser(user);

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(user));
        when(addressRepo.findByUserId(anyLong())).thenReturn(List.of());
        when(addressRepo.save(any(Address.class))).thenReturn(savedAddress);

        Address result = service.addAddress("test@gmail.com", dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetUserAddresses() {

        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        List<Address> addresses = List.of(new Address(), new Address());

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(user));
        when(addressRepo.findByUserId(anyLong())).thenReturn(addresses);

        List<Address> result = service.getUserAddresses("test@gmail.com");

        assertEquals(2, result.size());
    }

    @Test
    void testGetUserAddressById() {

        User user = new User();
        user.setId(1L);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(user));
        when(addressRepo.findById(anyLong())).thenReturn(Optional.of(address));

        Address result = service.getUserAddressesById("test@gmail.com", 1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateAddress() {

        AddressDto dto = new AddressDto();
        dto.setName("Updated Name");
        dto.setCity("Hyderabad");

        User user = new User();
        user.setId(1L);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(user));
        when(addressRepo.findById(anyLong())).thenReturn(Optional.of(address));
        when(addressRepo.save(any(Address.class))).thenReturn(address);

        Address result = service.updateAddress("test@gmail.com", 1L, dto);

        assertEquals("Updated Name", result.getName());
    }

    @Test
    void testDeleteAddress() {

        User user = new User();
        user.setId(1L);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(user));
        when(addressRepo.findById(anyLong())).thenReturn(Optional.of(address));

        MessageDto result = service.deleteAddress("test@gmail.com", 1L);

        assertEquals("address has been deleted successfully", result.getMessage());
    }

    @Test
    void testSetDefaultAddress() {

        User user = new User();
        user.setId(1L);

        Address address = new Address();
        address.setId(1L);
        address.setUser(user);

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(user));
        when(addressRepo.findById(anyLong())).thenReturn(Optional.of(address));
        when(addressRepo.findByUserId(anyLong())).thenReturn(List.of(address));
        when(addressRepo.save(any(Address.class))).thenReturn(address);

        Address result = service.setDefaultAddress("test@gmail.com", 1L);

        assertEquals(true, result.isDefault());
    }
}
