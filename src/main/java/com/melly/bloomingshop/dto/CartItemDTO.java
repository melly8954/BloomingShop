package com.melly.bloomingshop.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long productId;
    private String productName;
    private BigDecimal price; // 상품의 가격
    private String productSize;
    private String productImageUrl;
    private int quantity; // 상품의 수량
    private BigDecimal totalPrice; // 상품의 총 가격 (수량 * 가격)
}
