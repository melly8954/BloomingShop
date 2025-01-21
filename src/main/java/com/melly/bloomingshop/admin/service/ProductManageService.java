package com.melly.bloomingshop.admin.service;

import com.melly.bloomingshop.admin.dto.ProductManageRequest;
import com.melly.bloomingshop.domain.Category;
import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.repository.CategoryRepository;
import com.melly.bloomingshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductManageService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService; // 이미지 업로드 처리 서비스


    @Transactional
    public Product registerProduct(ProductManageRequest productManageRequest) throws IOException {
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
                .imageUrl(fileUploadService.uploadImage(productManageRequest.getImageUrl())) // 이미지 저장 로직
                .description(productManageRequest.getDescription())
                .categories(categories)
                .build();

        return productRepository.save(product);
    }
}
