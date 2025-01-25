package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByUser_Id(Long userId);      // 두개는 다르게 동작함 주의!
    Address findByUserId(Long userId);      // user_id로 주소를 조회하는 메서드
}
