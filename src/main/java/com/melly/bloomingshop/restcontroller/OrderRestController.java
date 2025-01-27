package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.dto.OrderListResponse;
import com.melly.bloomingshop.dto.OrderRequest;
import com.melly.bloomingshop.security.auth.PrincipalDetails;
import com.melly.bloomingshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderRestController implements ResponseController {
    private final OrderService orderService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> processOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // Order와 OrderItem 저장 처리
            orderService.createOrder(orderRequest);
            return makeResponseEntity(HttpStatus.OK, "주문 등록 성공", true);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류: " + ex.getMessage(), null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getOrders(@RequestHeader("Guest-Id") String guestId) {
        try{
            // 인증된 사용자 정보 가져오기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User userId = null;

            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                // 로그인한 사용자의 경우
                PrincipalDetails userDetails = (PrincipalDetails) auth.getPrincipal();
                userId = userDetails.getUser();
            }

            List<OrderListResponse> allOrders = orderService.getAllOrders(userId, guestId);
            return makeResponseEntity(HttpStatus.OK, "주문 내역 반환 성공", allOrders);
        }catch (Exception ex) {
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류: " + ex.getMessage(), null);
        }
    }
}