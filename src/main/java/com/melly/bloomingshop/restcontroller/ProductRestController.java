package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.dto.ProductPageResponse;
import com.melly.bloomingshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductRestController implements ResponseController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseDto> getProducts(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "price") String sort,  // 정렬 기준 (기본값: price)
            @RequestParam(defaultValue = "asc") String order) {  // 정렬 방향 (기본값: asc)
        try {
            // 페이지 번호와 크기 설정
            if (page <= 0) page = 1;
            if (size <= 0) size = 6;

            ProductPageResponse<Product> productPageResponse;

            if ("popularity".equals(sort)) {
                // 인기순 조회
                productPageResponse = productService.getProductsOrderedByPopularity(page, size);
            } else {
                // 일반적인 정렬
                Sort sortBy = Sort.by(Sort.Order.by(sort));
                sortBy = order.equalsIgnoreCase("desc") ? sortBy.descending() : sortBy.ascending();
                Pageable pageable = PageRequest.of(page - 1, size, sortBy);

                Page<Product> products = productService.getProducts(name, category, pageable);
                productPageResponse = new ProductPageResponse<>(
                        products.getContent(),
                        products.getTotalElements(),
                        products.getTotalPages(),
                        products.getNumber(),
                        products.getSize()
                );
            }

            return makeResponseEntity(HttpStatus.OK, "상품 목록을 성공적으로 조회했습니다.", productPageResponse);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청 파라미터: {}", e.getMessage(), e);
            return makeResponseEntity(HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다.", e.getMessage());
        } catch (Exception e) {
            log.error("상품 조회 중 오류 발생: {}", e.getMessage(), e);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "상품 조회 중 알 수 없는 오류가 발생했습니다.", e.getMessage());
        }
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ResponseDto> getProductDetail(@PathVariable Long id) {
        try{
            if(id == null|| id <= 0 ){
                return makeResponseEntity(HttpStatus.BAD_REQUEST,"상품 ID 오륲", null);
            }
            Product product = this.productService.findById(id);
            return makeResponseEntity(HttpStatus.OK, "상품 찾기 성공", product);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }
}
