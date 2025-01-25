package com.melly.bloomingshop.dto;

import com.melly.bloomingshop.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderRequest {
    private Long id;
    private String guestId;
    private String idType; // "userId" 또는 "guestId"
    private String shippingAddress; // 배송 주소
    private String paymentMethod; // 결제 방법
    private List<CartItemDto> cartItems; // 장바구니 아이템 목록
}
