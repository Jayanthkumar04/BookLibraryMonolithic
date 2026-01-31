package com.org.jayanth.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.jayanth.entity.Address;

public interface AddressRepo extends JpaRepository<Address,Long> {

	List<Address> findByUserId(Long userId);
}
