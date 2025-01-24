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
public class AddToCartRequest {
    private Long userId;    // 외래키 참조(userId 는 Long)
    private String guestId; // UUID -> String
    private Long productId;
    private int quantity;
}
