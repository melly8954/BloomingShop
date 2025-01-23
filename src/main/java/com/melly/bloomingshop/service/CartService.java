package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Cart;
import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.AddToCartRequest;
import com.melly.bloomingshop.dto.CartItemDTO;
import com.melly.bloomingshop.repository.CartRepository;
import com.melly.bloomingshop.repository.ProductRepository;
import com.melly.bloomingshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Cart addToCart(AddToCartRequest request) {
        // 사용자와 상품 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 ID 의 회원은 존재하지 않습니다."));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 ID 의 상품은 존재하지 않습니다."));

        // 장바구니에 이미 해당 상품이 있는지 확인
        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());

        if (existingCart.isPresent()) {
            // 이미 장바구니에 있으면 수량 업데이트
            Cart cart = existingCart.get();
            cart.changeQuantity(cart.getQuantity() + request.getQuantity());
            return cartRepository.save(cart); // 업데이트된 Cart 반환
        } else {
            // 없으면 새로 추가 (빌더 사용)
            Cart newCart = Cart.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            return cartRepository.save(newCart); // 새로 추가된 Cart 반환
        }
    }

    @Transactional
    public List<CartItemDTO> getCartItemsByUserId(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserIdWithProducts(userId);
        List<CartItemDTO> cartItemDTOs = new ArrayList<>();

        // Cart의 Product 정보와 수량을 DTO로 변환 (빌더 패턴 사용)
        for (Cart cart : cartItems) {
            CartItemDTO dto = CartItemDTO.builder()
                    .productId(cart.getProduct().getId())
                    .productName(cart.getProduct().getName())
                    .productPrice(cart.getProduct().getPrice())
                    .productSize(cart.getProduct().getSize())
                    .productImageUrl(cart.getProduct().getImageUrl())
                    .quantity(cart.getQuantity())
                    .build(); // 최종적으로 객체 생성
            cartItemDTOs.add(dto);
        }
        return cartItemDTOs;
    }
    
    // 장바구니 총 비용 비즈니스 로직
    @Transactional
    public BigDecimal updateCart(Long userId, List<CartItemDTO> cartItems) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (CartItemDTO item : cartItems) {
            // 장바구니 아이템 업데이트 (DB에 저장)
            Cart cartItem = cartRepository.findByUserIdAndProductId(userId, item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템을 찾을 수 없습니다."));

            cartItem.changeQuantity(item.getQuantity());
            cartRepository.save(cartItem);

            // 총합 계산
            BigDecimal itemTotal = cartItem.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalCost = totalCost.add(itemTotal);
        }

        return totalCost;
    }

}
