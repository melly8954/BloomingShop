package com.melly.bloomingshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class GuestCartItemDTO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String productSize;
    private String productImageUrl;
    private int quantity;
}


