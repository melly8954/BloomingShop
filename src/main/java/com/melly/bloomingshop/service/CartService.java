package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Cart;
import com.melly.bloomingshop.domain.GuestCart;
import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.AddToCartRequest;
import com.melly.bloomingshop.dto.CartItemDto;
import com.melly.bloomingshop.dto.GuestCartItemDTO;
import com.melly.bloomingshop.repository.CartRepository;
import com.melly.bloomingshop.repository.GuestCartRepository;
import com.melly.bloomingshop.repository.ProductRepository;
import com.melly.bloomingshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final GuestCartRepository guestCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Cart addToCart(AddToCartRequest request, Long userId) {
        // 회원 장바구니 로직
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 회원은 존재하지 않습니다."));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품은 존재하지 않습니다."));

        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.changeQuantity(cart.getQuantity() + request.getQuantity());
            return cartRepository.save(cart);
        } else {
            Cart newCart = Cart.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            return cartRepository.save(newCart);
        }
    }

    @Transactional
    public GuestCart addToCartForGuest(AddToCartRequest request, String guestId) {
        // 비회원 장바구니 로직
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품은 존재하지 않습니다."));

        Optional<GuestCart> existingGuestCart = guestCartRepository.findByGuestIdAndProductId(guestId, request.getProductId());
        if (existingGuestCart.isPresent()) {
            GuestCart guestCart = existingGuestCart.get();
            guestCart.changeQuantity(guestCart.getQuantity() + request.getQuantity());
            return guestCartRepository.save(guestCart);
        } else {
            GuestCart newGuestCart = GuestCart.builder()
                    .guestId(guestId)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            return guestCartRepository.save(newGuestCart);
        }
    }

    public List<CartItemDto> getCartItemsByUserId(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserIdWithProducts(userId);
        List<CartItemDto> cartItemDtos = new ArrayList<>();

        // Cart의 Product 정보와 수량을 DTO로 변환 (빌더 패턴 사용)
        for (Cart cart : cartItems) {
            CartItemDto dto = CartItemDto.builder()
                    .productId(cart.getProduct().getId())
                    .productName(cart.getProduct().getName())
                    .price(cart.getProduct().getPrice())
                    .productSize(cart.getProduct().getSize())
                    .productImageUrl(cart.getProduct().getImageUrl())
                    .quantity(cart.getQuantity())
                    .build(); // 최종적으로 객체 생성
            cartItemDtos.add(dto);
        }
        return cartItemDtos;
    }

    // 비회원 장바구니 아이템 가져오기 비즈니스 로직
    public List<GuestCartItemDTO> getCartItemsByGuestId(String guestId) {
        List<GuestCart> cartItems = guestCartRepository.findByGuestId(guestId);
        List<GuestCartItemDTO> guestCartItemDTOs = new ArrayList<>();

        // GuestCart의 Product 정보와 수량을 DTO로 변환 (빌더 패턴 사용)
        for (GuestCart cart : cartItems) {
            GuestCartItemDTO dto = GuestCartItemDTO.builder()
                    .productId(cart.getProduct().getId())
                    .productName(cart.getProduct().getName())
                    .price(cart.getProduct().getPrice())
                    .productSize(cart.getProduct().getSize())
                    .productImageUrl(cart.getProduct().getImageUrl())
                    .quantity(cart.getQuantity())
                    .build(); // 최종적으로 객체 생성
            guestCartItemDTOs.add(dto);
        }
        return guestCartItemDTOs;
    }

    // 로그인 회원 장바구니 상품 제거 비즈니스 로직
    public boolean removeProductFromUserCart(Long userId, Long productId) {
        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, productId);
        if(existingCart.isPresent()) {
            cartRepository.delete(existingCart.get());
            return true;
        }
        log.error("찾는 회원의 장바구니가 존재하지 않습니다.");
        return false;
    }

    // 비회원 장바구니 상품 제거 비즈니스 로직
    public boolean removeProductFromGuestCart(String guestId, Long productId) {
        Optional<GuestCart> existingCart = guestCartRepository.findByGuestIdAndProductId(guestId,productId);
        if(existingCart.isPresent()) {
            guestCartRepository.delete(existingCart.get());
            return true;
        }
        log.error("찾는 비회원의 장바구니가 존재하지 않습니다.");
        return false;
    }

    // 로그인 유저의 장바구니 총 비용 비즈니스 로직
    public BigDecimal updateUserCart(Long userId, List<CartItemDto> cartItems) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (CartItemDto item : cartItems) {
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

    // 비고르인 유저의 장바구니 총 비용 비즈니스 로직
    public BigDecimal updateGuestCart(String guestId, List<CartItemDto> cartItems) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (CartItemDto item : cartItems) {
            // 비회원 장바구니 아이템을 찾고, 수량 업데이트
            GuestCart guestCartItem = guestCartRepository.findByGuestIdAndProductId(guestId, item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템을 찾을 수 없습니다."));

            guestCartItem.changeQuantity(item.getQuantity()); // 수량 변경
            guestCartRepository.save(guestCartItem);

            // 총합 계산
            BigDecimal itemTotal = guestCartItem.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(guestCartItem.getQuantity()));
            totalCost = totalCost.add(itemTotal);
        }

        return totalCost;
    }

}
