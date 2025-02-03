package com.melly.bloomingshop.admin.restcontroller;

import com.melly.bloomingshop.admin.service.OrderManageService;
import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Order;
import com.melly.bloomingshop.domain.Product;
import com.melly.bloomingshop.dto.OrderPageResponse;
import com.melly.bloomingshop.dto.ProductPageResponse;
import com.melly.bloomingshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/order")
public class OrderManageController implements ResponseController {
    private final OrderManageService orderManageService;

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "orderId") String sort,  // 정렬 기준
            @RequestParam(defaultValue = "asc") String sortOrder) {  // 정렬 방향 (기본값: asc)
        try{
            // 페이지 번호와 크기 설정
            if (page <= 0) page = 1;
            if (size <= 0) size = 5;
            // 정렬 설정
            Sort sortBy = Sort.by(Sort.Order.by(sort));
            sortBy = sortOrder.equalsIgnoreCase("desc") ? sortBy.descending() : sortBy.ascending();
            Pageable pageable = PageRequest.of(page - 1, size, sortBy);

            // 주문 리스트 가져오기
            Page<Order> orders = orderManageService.getOrders(pageable);

            // 응답 객체 생성
            OrderPageResponse<Order> orderPageResponse = new OrderPageResponse<>(
                    orders.getContent(),
                    orders.getTotalElements(),
                    orders.getTotalPages(),
                    orders.getNumber(),
                    orders.getSize()
            );

            return makeResponseEntity(HttpStatus.OK, "모든 회원들의 주문 항목을 성공적으로 조회 했습니다.", orderPageResponse);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 요청 파라미터: {}", e.getMessage(), e);
            return makeResponseEntity(HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다.", e.getMessage());
        } catch (Exception e) {
            log.error("상품 조회 중 오류 발생: {}", e.getMessage(), e);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "상품 조회 중 알 수 없는 오류가 발생했습니다.", e.getMessage());
        }
    }
}
