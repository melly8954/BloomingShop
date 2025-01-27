package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.GuestCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestCartRepository extends JpaRepository<GuestCart, Long> {
    Optional<GuestCart> findByGuestIdAndProductId(String guestId, Long productId);
    List<GuestCart> findByGuestId(String guestId);

    // guestId로 장바구니 항목 삭제
    void deleteByGuestId(String guestId);
}
