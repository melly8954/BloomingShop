package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
