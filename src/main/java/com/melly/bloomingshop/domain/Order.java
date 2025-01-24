package com.melly.bloomingshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_tbl")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;  // 회원 주문을 위한 유저 정보

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;  // 주소 테이블과의 관계

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "order_date")
    private
    LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "shipping_address_non_member")
    private String shippingAddressNonMember;  // 비회원 주소

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "delivery_status_updated")
    private LocalDateTime deliveryStatusUpdated;

    @Column(name = "payment_status_updated")
    private LocalDateTime paymentStatusUpdated;
}
