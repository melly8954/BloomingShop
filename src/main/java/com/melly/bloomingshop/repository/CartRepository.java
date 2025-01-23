package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 사용자별로 장바구니 아이템 찾기
    List<Cart> findByUserId(Long userId);

    // 사용자와 상품에 따른 장바구니 찾기
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
}
