package com.melly.bloomingshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ProductPopularityResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String size;
    private String imageUrl;
    private String description;
    private Boolean deletedFlag;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deletedDate;
    private Long orderCount; // 주문 횟
}
