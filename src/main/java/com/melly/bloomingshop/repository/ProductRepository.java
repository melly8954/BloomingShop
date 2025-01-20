package com.melly.bloomingshop.repository;

import com.melly.bloomingshop.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 상품명으로 검색
    Page<Product> findByNameContaining(String name, Pageable pageable);

    // 카테고리명으로 필터링된 상품 조회
    Page<Product> findByCategories_Name(String categoryName, Pageable pageable);

    // 상품명 검색 + 카테고리 필터링
    Page<Product> findByNameContainingAndCategories_Name(String name, String categoryName, Pageable pageable);
}
