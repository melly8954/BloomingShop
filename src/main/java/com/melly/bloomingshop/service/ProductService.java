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
    private final CategoryRepository categoryRepository;

    // 검색, 필터링, 정렬 통합 메서드
    public Page<Product> getProducts(String name, String category, Pageable pageable) {
        // 검색 조건과 필터 조건이 모두 없는 경우 모든 상품 반환
        if ((name == null || name.isEmpty()) && (category == null || category.isEmpty())) {

            Page<Product> products = productRepository.findAll(pageable);
            log.info("Products fetched: {}", products.getContent());
            return products;
        }

        // 상품명 검색과 카테고리 필터를 모두 포함
        if (name != null && !name.isEmpty() && category != null && !category.isEmpty()) {
            return productRepository.findByNameContainingAndCategories_Name(name, category, pageable);
        }

        // 상품명 검색만 있는 경우
        if (name != null && !name.isEmpty()) {
            return productRepository.findByNameContaining(name, pageable);
        }

        // 카테고리 필터만 있는 경우
        return productRepository.findByCategories_Name(category, pageable);
    }

}
