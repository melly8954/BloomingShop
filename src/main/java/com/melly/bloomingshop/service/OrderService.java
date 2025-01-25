package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.*;
import com.melly.bloomingshop.dto.CartItemDto;
import com.melly.bloomingshop.dto.OrderRequest;
import com.melly.bloomingshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;  // UserRepository 추가

    @Transactional
    public void createOrder(OrderRequest orderRequest) {
        // 1. Order 엔티티 생성 (totalPrice는 일단 0으로 설정)
        Order order = Order.builder()
                .paymentMethod(orderRequest.getPaymentMethod())  // 결제 방법
                .totalPrice(BigDecimal.ZERO)  // 일단 totalPrice는 0으로 설정
                .deliveryStatus("배송 준비 중")
                .paymentStatus("결제 진행 중")
                .build();

        // 로그인한 경우 주소 처리
        if (orderRequest.getIdType().equals("userId") && orderRequest.getId() != null) {
            User user = userRepository.findById(orderRequest.getId())  // User 객체 조회
                    .orElseThrow(() -> new RuntimeException("User not found for id: " + orderRequest.getId()));

            order.changeUser(user);  // Order에 User 객체 설정

            Address address = addressRepository.findByUserId(orderRequest.getId());
            if (address != null) {
                order.changeAddress(address);  // 주소를 address_id 컬럼에 설정
            } else {
                throw new RuntimeException("Address not found for userId: " + orderRequest.getId());
            }
        } else {
            order.changeGuest(orderRequest.getGuestId());
            // 비회원 주문 시 주소를 shippingAddressNonMember에 설정
            order.changeShippingAddressNonMember(orderRequest.getShippingAddress());
        }

        // 2. 총 결제 금액을 계산할 변수
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 3. Order 엔티티 저장 (먼저 저장)
        orderRepository.save(order);

        // 4. OrderItem 엔티티 생성
        for (CartItemDto cartItem : orderRequest.getCartItems()) {
            // Product 조회
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found for id: " + cartItem.getProductId()));

            // OrderItem 생성
            OrderItem orderItem = OrderItem.builder()
                    .orderId(order)  // Order 객체 (orderId가 자동으로 설정됨)
                    .productId(product)  // Product 객체
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())  // price 값 제대로 설정
                    .totalPrice(cartItem.getTotalPrice())
                    .build();

            totalPrice = totalPrice.add(cartItem.getTotalPrice());  // totalPrice 합산

            // OrderItem을 저장
            orderItemRepository.save(orderItem);
        }

        // 5. 총 결제 금액을 계산한 후, Order 엔티티의 totalPrice를 설정
        order.changeTotalPrice(totalPrice);

        // 6. Order 엔티티 저장 (총 결제 금액 업데이트 후)
        orderRepository.save(order);
    }

}