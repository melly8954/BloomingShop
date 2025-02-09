package com.melly.bloomingshop.admin.service;

import com.melly.bloomingshop.admin.dto.ProductManageRequest;
import com.melly.bloomingshop.admin.dto.ProductModifyRequest;
import com.melly.bloomingshop.domain.Category;
import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.repository.CategoryRepository;
import com.melly.bloomingshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductManageService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService; // 이미지 업로드 처리 서비스

    // 검색, 필터링, 정렬 통합 메서드
    public Page<Product> getProducts(String name, String category, Pageable pageable) {
        // 검색 조건과 필터 조건이 모두 없는 경우 모든 상품 반환
        if ((name == null || name.isEmpty()) && (category == null || category.isEmpty())) {
            // deleted_flag가 false인 상품만 가져오기
            Page<Product> products = productRepository.findAll(pageable);
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

    @Transactional
    public Product registerProduct(ProductManageRequest productManageRequest, MultipartFile image) throws IOException {
        // 1. 단일 카테고리 이름을 가져옵니다.
        String categoryName = productManageRequest.getCategory();

        // 2. 카테고리를 찾아옵니다.
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new RuntimeException("카테고리가 존재하지 않습니다: " + categoryName);
        }
        Set<Category> categories = new HashSet<>();
        categories.add(category);  // 카테고리를 Set에 추가

        Product product = Product.builder()
                .name(productManageRequest.getName())
                .price(productManageRequest.getPrice())
                .size(productManageRequest.getSize())
                .imageUrl(fileUploadService.saveProductImage(image)) // 이미지 저장 로직
                .description(productManageRequest.getDescription())
                .categories(categories)
                .deletedFlag(false)
                .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product modifyProduct(ProductModifyRequest productModifyRequest, MultipartFile image) throws IOException {
        // 기존 상품 조회
        Product existingProduct = productRepository.findById(productModifyRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품이 존재하지 않습니다: " + productModifyRequest.getProductId()));

        // 카테고리 조회 및 설정
        String categoryName = productModifyRequest.getCategory();
        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new RuntimeException("카테고리가 존재하지 않습니다: " + categoryName);
        }
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        existingProduct.setCategories(categories); // categories는 별도로 설정

        // DTO의 필드를 기존 상품에 복사
        existingProduct.copyFields(productModifyRequest);

        // 이미지가 새로 업로드된 경우 처리
        if (image != null && !image.isEmpty()) {
            String imageUrl = fileUploadService.saveProductImage(image);
            existingProduct.modifyImageUrl(imageUrl);
        }
        existingProduct.modifyUpdatedDate(LocalDateTime.now());
        // 수정된 상품 저장
        return productRepository.save(existingProduct);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
    }
    
    // softDelete 비즈니스 로직
    public void softDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + id + "인 상품을 찾을 수 없습니다."));

        // 소프트 삭제 처리
        product.changeDeletedFlag(true);
        product.modifyDeletedDate(LocalDateTime.now());

        productRepository.save(product);
    }

    // softDelete Restore 비즈니스 로직
    @Transactional
    public void restoreProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        if (!product.getDeletedFlag()) {
            throw new IllegalStateException("이미 활성화된 상품입니다.");
        }
        product.changeDeletedFlag(false); // 삭제 취소
        product.modifyDeletedDate(null);
    }
}
