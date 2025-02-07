package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.dto.ProductPopularityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // deleted_flag가 false인 상품만 조회 (기본 전체 조회)
    Page<Product> findByDeletedFlagFalse(Pageable pageable);

    // 상품명으로 검색
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByNameContainingAndDeletedFlagFalse(String name, Pageable pageable);

    // 카테고리명으로 필터링된 상품 조회
    Page<Product> findByCategories_Name(String categoryName, Pageable pageable);
    Page<Product> findByCategories_NameAndDeletedFlagFalse(String categoryName, Pageable pageable);

    // 상품명 검색 + 카테고리 필터링
    Page<Product> findByNameContainingAndCategories_Name(String name, String categoryName, Pageable pageable);
    Page<Product> findByNameContainingAndCategories_NameAndDeletedFlagFalse(String name, String categoryName, Pageable pageable);

    @Query("SELECT new com.melly.bloomingshop.dto.ProductPopularityResponse(" +
            "p.id, p.name, p.price, p.size, p.imageUrl, p.description, " +
            "p.deletedFlag, p.createdDate, p.updatedDate, p.deletedDate, " +
            "COUNT(DISTINCT oi.orderId)) " +
            "FROM Product p " +
            "LEFT JOIN OrderItem oi ON p.id = oi.productId.id " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(DISTINCT oi.orderId) DESC")
    Page<ProductPopularityResponse> findProductsOrderedByPopularity(Pageable pageable);
}
