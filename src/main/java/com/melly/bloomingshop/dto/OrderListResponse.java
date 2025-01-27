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
}
