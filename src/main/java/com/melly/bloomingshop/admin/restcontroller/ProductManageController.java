package com.melly.bloomingshop.admin.restcontroller;

import com.melly.bloomingshop.admin.dto.ProductManageRequest;
import com.melly.bloomingshop.admin.dto.ProductModifyRequest;
import com.melly.bloomingshop.admin.service.ProductManageService;
import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Category;
import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.dto.ProductPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/product")
public class ProductManageController implements ResponseController {
    private final ProductManageService productManageService;

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

            // 정렬 설정
            Sort sortBy = Sort.by(Sort.Order.by(sort));
            sortBy = order.equalsIgnoreCase("desc") ? sortBy.descending() : sortBy.ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sortBy);

            // 상품 데이터 가져오기
            Page<Product> products = productManageService.getProducts(name, category, pageable);

            // 응답 객체 생성
            ProductPageResponse<Product> productPageResponse = new ProductPageResponse<>(
                    products.getContent(),
                    products.getTotalElements(),
                    products.getTotalPages(),
                    products.getNumber(),
                    products.getSize()
            );
            return makeResponseEntity(HttpStatus.OK, "상품 목록을 성공적으로 조회했습니다.", productPageResponse);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청 파라미터: {}", e.getMessage(), e);
            return makeResponseEntity(HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다.", e.getMessage());
        } catch (Exception e) {
            log.error("상품 조회 중 오류 발생: {}", e.getMessage(), e);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "상품 조회 중 알 수 없는 오류가 발생했습니다.", e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addProduct(@Validated @ModelAttribute ProductManageRequest productManageRequest, BindingResult bindingResult,
                                                     @RequestPart(value = "image", required = false) MultipartFile image ) {
        //  유효성 검사는 데이터가 비즈니스 로직에 들어가기 전에 검증하는 것
        if(bindingResult.hasErrors()){
            // 유효성 검사를 실패(@Size , @NotBlank 조건 실패 시) 하면 BindingResult 객체에 오류가 담긴다.
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(" / ");
            });
            log.error("유효성 검사 실패" + errorMessages);
            return makeResponseEntity(HttpStatus.BAD_REQUEST,errorMessages.toString(), null);
        }
        try{
            Product product = productManageService.registerProduct(productManageRequest,image);
            return makeResponseEntity(HttpStatus.OK,"상품 등록 성공",product);
        }catch (Exception ex){
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러 :" + ex.getMessage(), null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getProduct(@PathVariable Long id){
        try{
            if(id == null || id <= 0){
                return makeResponseEntity(HttpStatus.BAD_REQUEST,"상품 ID 에러",null);
            }
            Product product = this.productManageService.findById(id);
            return makeResponseEntity(HttpStatus.OK,"상품 찾기 성공",product);
        }catch (Exception ex){
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러 :" + ex.getMessage(), null);
        }
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<ResponseDto> getCategories(@PathVariable Long id){
        try{
            if(id == null || id <= 0){
                return makeResponseEntity(HttpStatus.BAD_REQUEST,"상품 ID 에러",null);
            }
            // 상품을 조회
            Product product = productManageService.findById(id);

            if (product == null) {
                return makeResponseEntity(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.", null);
            }

            // 카테고리 목록을 포함하여 응답 생성
            Set<Category> categories = product.getCategories();

            // 카테고리 정보만 반환
            return makeResponseEntity(HttpStatus.OK, "상품 카테고리 조회 성공", categories);
        }catch (Exception ex){
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러 :" + ex.getMessage(), null);
        }
    }

    @PatchMapping("/modify")
    public ResponseEntity<ResponseDto> modifyProduct(@Validated @ModelAttribute ProductModifyRequest productModifyRequest, BindingResult bindingResult,
                                                        @RequestPart(value = "image", required = false) MultipartFile image) {
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessages.append(error.getDefaultMessage()).append(" / ");
            });
            log.error("유효성 검사 실패: " + errorMessages);
            return makeResponseEntity(HttpStatus.BAD_REQUEST, errorMessages.toString(), null);
        }
        try {
            Product updatedProduct = productManageService.modifyProduct(productModifyRequest, image);
            return makeResponseEntity(HttpStatus.OK, "상품 수정 성공", updatedProduct);
        } catch (Exception ex) {
            log.error("상품 수정 실패: " + ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }

    // softDelete API
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> softDeleteProduct(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "상품 ID가 유효하지 않습니다.", null);
            }
            productManageService.softDeleteProduct(id);
            return makeResponseEntity(HttpStatus.OK, "상품이 정상적으로 삭제되었습니다.", true);
        } catch (IllegalArgumentException ex) {
            log.error("상품 삭제 실패: " + ex.getMessage());
            return makeResponseEntity(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다: " + ex.getMessage(), null);
        } catch (Exception ex) {
            log.error("상품 삭제 중 서버 에러 발생: " + ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }

    // // softDelete restore API
    @PatchMapping("/{productId}/restore")
    public ResponseEntity<?> restoreProduct(@PathVariable Long productId) {
        try {
            if (productId == null || productId <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "상품 ID가 유효하지 않습니다.", null);
            }
            productManageService.restoreProduct(productId); // 서비스 호출
            return makeResponseEntity(HttpStatus.OK, "상품 삭제가 성공적으로 취소되었습니다.", true);
        } catch (IllegalArgumentException ex) {
            log.error("상품 삭제 취소 실패: " + ex.getMessage());
            return makeResponseEntity(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다: " + ex.getMessage(), null);
        } catch (Exception ex) {
            log.error("상품 삭제 취소 중 서버 에러 발생: " + ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }
}
