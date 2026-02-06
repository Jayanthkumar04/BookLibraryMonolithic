package com.org.jayanth.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.jayanth.dto.AddressDto;
import com.org.jayanth.dtobestprac.MessageDto;
import com.org.jayanth.entity.Address;
import com.org.jayanth.entity.User;
import com.org.jayanth.exceptions.AddressNotFoundException;
import com.org.jayanth.exceptions.UserNotFoundException;
import com.org.jayanth.exceptions.UserUnauthorizedPageException;
import com.org.jayanth.repo.AddressRepo;
import com.org.jayanth.repo.UserRepo;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Override
    public Address addAddress(String email, AddressDto dto) {

        logger.info("Add address initiated | user={}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

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

        if (dto.isDefault()) {
            clearDefaultAddress(user.getId());
            address.setDefault(true);
        }

        Address saved = addressRepo.save(address);

        logger.info("Add address successful | user={} addressId={}", email, saved.getId());

        return saved;
    }

    private void clearDefaultAddress(Long id) {

        logger.info("Clear default address initiated | userId={}", id);

        List<Address> addresses = addressRepo.findByUserId(id);

        for (Address a : addresses) {
            a.setDefault(false);
            addressRepo.save(a);
        }

        logger.info("Clear default address successful | userId={}", id);
    }

    @Override
    public List<Address> getUserAddresses(String email) {

        logger.info("Get user addresses initiated | user={}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        List<Address> addresses = addressRepo.findByUserId(user.getId());

        logger.info("Get user addresses successful | user={} count={}", email, addresses.size());

        return addresses;
    }

    @Override
    public Address updateAddress(String email, Long addressId, AddressDto dto) {

        logger.info("Update address initiated | user={} addressId={}", email, addressId);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

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

        Address updated = addressRepo.save(address);

        logger.info("Update address successful | user={} addressId={}", email, addressId);

        return updated;
    }

    @Override
    public MessageDto deleteAddress(String email, Long addressId) {

        logger.info("Delete address initiated | user={} addressId={}", email, addressId);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new UserUnauthorizedPageException("Unauthorized");
        }

        addressRepo.delete(address);

        logger.info("Delete address successful | user={} addressId={}", email, addressId);

        return new MessageDto("address has been deleted successfully");
    }

    @Override
    public Address setDefaultAddress(String email, Long addressId) {

        logger.info("Set default address initiated | user={} addressId={}", email, addressId);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new UserUnauthorizedPageException("Unauthorized");
        }

        clearDefaultAddress(user.getId());
        address.setDefault(true);

        Address updated = addressRepo.save(address);

        logger.info("Set default address successful | user={} addressId={}", email, addressId);

        return updated;
    }

    @Override
    public Address getUserAddressesById(String email, Long id) {

        logger.info("Get address by id initiated | user={} addressId={}", email, id);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Address address = addressRepo.findById(id)
                .orElseThrow(() -> new AddressNotFoundException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new UserUnauthorizedPageException("Unauthorized");
        }

        logger.info("Get address by id successful | user={} addressId={}", email, id);

        return address;
    }
}
