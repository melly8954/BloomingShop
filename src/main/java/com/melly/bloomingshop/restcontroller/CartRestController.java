package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Cart;
import com.melly.bloomingshop.dto.AddToCartRequest;
import com.melly.bloomingshop.dto.CartItemDTO;
import com.melly.bloomingshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartRestController implements ResponseController {
    private final CartService cartService;
    
    // 장바구니에 상품 추가
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addToCart(@RequestBody AddToCartRequest request) {
        try{
            Cart cart = this.cartService.addToCart(request);
            return makeResponseEntity(HttpStatus.OK, "장바구니 등록 완료", cart);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }
    
    // 장바구니 목록 출력
    @GetMapping("/{userId}/list")
    public ResponseEntity<ResponseDto> getCartItems(@PathVariable Long userId) {
        try{
            if(userId == null || userId <= 0){
                log.error("찾으시는 회원 ID가 존재하지 않습니다.");
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "잘못된 회원 ID 입니다.", null);
            }
            List<CartItemDTO> cartItems = cartService.getCartItemsByUserId(userId);
            return makeResponseEntity(HttpStatus.OK, "장바구니 리스트 검색 완료", cartItems);
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }

    // 장바구니에서 물건 제거 API
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<ResponseDto> deleteCart(@PathVariable Long userId, @PathVariable Long productId) {
        try{
            boolean isValid = cartService.removeProductFromCart(userId, productId);
            return makeResponseEntity(HttpStatus.OK, "상품이 장바구니에서 제거되었습니다.", isValid);
        }catch (Exception ex){
            log.error("장바구니 저장 중 오류 발생: {}", ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }

    @PostMapping("/{userId}/total")
    public ResponseEntity<ResponseDto> calculateAndSaveCart(@PathVariable Long userId, @RequestBody List<CartItemDTO> cartItems) {
        try {
            if (userId == null || userId <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 ID입니다.", null);
            }
            // 장바구니 데이터 저장 및 총합 계산
            BigDecimal totalCost = cartService.updateCart(userId, cartItems);

            return makeResponseEntity(HttpStatus.OK, "장바구니 계산 완료", Map.of("totalCost", totalCost));
        } catch (Exception ex) {
            log.error("장바구니 저장 중 오류 발생: {}", ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }
}
