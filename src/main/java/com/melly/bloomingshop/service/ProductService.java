package com.melly.bloomingshop.service;

import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.dto.ProductPageResponse;
import com.melly.bloomingshop.dto.ProductPopularityResponse;
import com.melly.bloomingshop.repository.CategoryRepository;
import com.melly.bloomingshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    // 인기순 정렬 비즈니스 로직
    public ProductPageResponse<Product> getProductsOrderedByPopularity(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // 인기순 정렬을 포함한 페이지네이션된 결과 가져오기
        Page<ProductPopularityResponse> results = productRepository.findProductsOrderedByPopularity(pageable);

        // ProductPopularityResponse에서 Product로 변환
        List<Product> products = results.getContent().stream().map(result -> {
            return Product.builder()
                    .id(result.getId())
                    .name(result.getName())
                    .price(result.getPrice())
                    .size(result.getSize())
                    .imageUrl(result.getImageUrl())
                    .description(result.getDescription())
                    .deletedFlag(result.getDeletedFlag())
                    .createdDate(result.getCreatedDate())
                    .updatedDate(result.getUpdatedDate())
                    .deletedDate(result.getDeletedDate())
                    .build();
        }).collect(Collectors.toList());

        return new ProductPageResponse<>(products, results.getTotalElements(), results.getTotalPages(), page, size);
    }
}
