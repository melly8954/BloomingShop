package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Order;
import com.melly.bloomingshop.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Order orderId);
}
