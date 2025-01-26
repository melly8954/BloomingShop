package com.melly.bloomingshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderListResponse {
    private String productName;
    private String productPrice;
    private String productSize;
    private String productImageUrl;
    private int quantity;
    private int totalPrice;
    private String paymentStatus;
    private String deliveryStatus;
}
