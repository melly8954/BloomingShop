package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Order;
import com.melly.bloomingshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdAndDeletedFlagFalse(User userId);   // 로그인한 유저의 삭제되지 않은 주문 내역 조회
    List<Order> findByGuestIdAndDeletedFlagFalse(String guestId); // 비로그인 유저의 삭제되지 않은 주문 내역 조회
}
