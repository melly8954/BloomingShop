package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.repository.CategoryRepository;
import com.melly.bloomingshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 검색, 필터링, 정렬 통합 메서드
    public Page<Product> getProducts(String name, String category, Pageable pageable) {
        // 검색 조건과 필터 조건이 모두 없는 경우 모든 상품 반환 (deleted_flag 체크)
        if ((name == null || name.isEmpty()) && (category == null || category.isEmpty())) {
            // deleted_flag가 false인 상품만 가져오기
            Page<Product> products = productRepository.findByDeletedFlagFalse(pageable);
            log.info("Products fetched: {}", products.getContent());
            return products;
        }

        // 상품명 검색과 카테고리 필터를 모두 포함 (deleted_flag 체크)
        if (name != null && !name.isEmpty() && category != null && !category.isEmpty()) {
            return productRepository.findByNameContainingAndCategories_NameAndDeletedFlagFalse(name, category, pageable);
        }

        // 상품명 검색만 있는 경우 (deleted_flag 체크)
        if (name != null && !name.isEmpty()) {
            return productRepository.findByNameContainingAndDeletedFlagFalse(name, pageable);
        }

        // 카테고리 필터만 있는 경우 (deleted_flag 체크)
        return productRepository.findByCategories_NameAndDeletedFlagFalse(category, pageable);
    }

    // 상품 ID 로 상품 찾기
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
    }
}
