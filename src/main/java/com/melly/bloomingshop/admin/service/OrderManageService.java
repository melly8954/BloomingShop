package com.melly.bloomingshop.admin.service;

import com.melly.bloomingshop.domain.Order;
import com.melly.bloomingshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderManageService {
    private final OrderRepository orderRepository;

    public Page<Order> getOrders(Pageable pageable) {
        Page<Order> orders = this.orderRepository.findByDeletedFlagFalse(pageable);
        return orders;
    }
}
