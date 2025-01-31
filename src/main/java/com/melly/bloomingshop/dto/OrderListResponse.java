package com.melly.bloomingshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderListResponse {
    private Long orderId;
    private String productName;
    private BigDecimal productPrice;
    private String productSize;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal singleItemTotalPrice;
    private BigDecimal totalOrderPrice;
    private String paymentStatus;
    private String deliveryStatus;
    private String userAddress;  // 로그인 유저의 주소 (address_tbl에서 가져옴)
    private String guestAddress; // 비로그인 유저의 주소 (order_tbl에서 가져옴)
}
