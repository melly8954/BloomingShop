package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Cart;
import com.melly.bloomingshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // 사용자의 user_id 컬럼으로 장바구니 아이템 찾기
    @Query("SELECT c FROM Cart c JOIN FETCH c.product WHERE c.user.id = :userId")
    List<Cart> findByUserIdWithProducts(@Param("userId") Long userId);

    // 사용자 ID, 상품ID를 통한  장바구니 찾기
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    // 사용자 user_id 컬럼으로 장바구니 항목 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}
