package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Cart;
import com.melly.bloomingshop.dto.AddToCartRequest;
import com.melly.bloomingshop.dto.CartItemDTO;
import com.melly.bloomingshop.dto.GuestCartItemDTO;
import com.melly.bloomingshop.security.auth.PrincipalDetails;
import com.melly.bloomingshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 장바구니에 상품추가 API
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addToCart(@RequestBody AddToCartRequest request) {
        try {
            Long userId = null; // 회원 ID
            String guestId = null; // 비회원 ID

            // 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
                // 로그인한 사용자의 userId 가져오기
                Object principal = authentication.getPrincipal();
                if (principal instanceof PrincipalDetails) {
                    PrincipalDetails principalDetails = (PrincipalDetails) principal;
                    userId = principalDetails.getUser().getId();
                } else {
                    throw new RuntimeException("잘못된 인증 정보입니다. PrincipalDetails 객체가 아닙니다.");
                }
            } else {
                // 비회원 요청의 경우 guestId를 사용
                guestId = request.getGuestId();
                if (guestId == null || guestId.isEmpty()) {
                    throw new RuntimeException("비회원 ID(guestId)가 제공되지 않았습니다.");
                }
            }

            // 장바구니에 상품 추가 (userId와 guestId 구분)
            Object cart;
            if (userId != null) {
                // 회원의 경우 userId 전달
                cart = cartService.addToCart(request, userId);
            } else {
                // 비회원의 경우 guestId 전달
                cart = cartService.addToCartForGuest(request, guestId);
            }

            return makeResponseEntity(HttpStatus.OK, "장바구니 등록 완료", cart);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }
    
    // 장바구니 목록 출력
    @GetMapping("/user/{userId}/list")
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

    // 비회원 장바구니 목록 출력
    @GetMapping("/guest/{guestId}/list")
    public ResponseEntity<ResponseDto> getGuestCartItems(@PathVariable String guestId) {
        try {
            if (guestId == null || guestId.trim().isEmpty()) {
                log.error("찾으시는 게스트 ID가 유효하지 않습니다.");
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "잘못된 게스트 ID입니다.", null);
            }
            List<GuestCartItemDTO> guestCartItems = cartService.getCartItemsByGuestId(guestId);
            return makeResponseEntity(HttpStatus.OK, "게스트 장바구니 리스트 검색 완료", guestCartItems);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }

    // 로그인 유저 장바구니에서 물건 제거 API
    @DeleteMapping("/user/{userId}/{productId}")
    public ResponseEntity<ResponseDto> deleteCart(@PathVariable Long userId, @PathVariable Long productId) {
        try{
            boolean isValid = cartService.removeProductFromUserCart(userId, productId);
            return makeResponseEntity(HttpStatus.OK, "상품이 장바구니에서 제거되었습니다.", isValid);
        }catch (Exception ex){
            log.error("장바구니 저장 중 오류 발생: {}", ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }

    // 비회원 장바구니에서 물건 제거 API
    @DeleteMapping("/guest/{guestId}/{productId}")
    public ResponseEntity<ResponseDto> deleteGuestCartItem(@PathVariable String guestId, @PathVariable Long productId) {
        try {
            boolean isValid = cartService.removeProductFromGuestCart(guestId, productId);
            return makeResponseEntity(HttpStatus.OK, "상품이 비회원의 장바구니에서 제거되었습니다.", isValid);
        } catch (Exception ex) {
            log.error("비회원 장바구니에서 상품 제거 중 오류 발생: {}", ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }

    // 로그인 유저 장바구니 상품들의 총 비용 API
    @PostMapping("/user/{userId}/total")
    public ResponseEntity<ResponseDto> getUserCartTotal(@PathVariable Long userId, @RequestBody List<CartItemDTO> cartItems) {
        try {
            if (userId == null || userId <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 ID입니다.", null);
            }
            // 장바구니 데이터 저장 및 총합 계산
            BigDecimal totalCost = cartService.updateUserCart(userId, cartItems);

            return makeResponseEntity(HttpStatus.OK, "장바구니 계산 완료", Map.of("totalCost", totalCost));
        } catch (Exception ex) {
            log.error("장바구니 저장 중 오류 발생: {}", ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러: " + ex.getMessage(), null);
        }
    }

    // 비로그인 유저 장바구니 상품들의 총 비용 API
    @PostMapping("/guest/{guestId}/total")
    public ResponseEntity<ResponseDto> getGuestCartTotal(@PathVariable String guestId, @RequestBody List<CartItemDTO> cartItems) {
        try {
            // 비회원 장바구니 총 가격 계산
            BigDecimal totalCost = cartService.updateGuestCart(guestId, cartItems);
            return makeResponseEntity(HttpStatus.OK, "장바구니 총 가격 계산 완료",  Map.of("totalCost", totalCost));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 : " + ex.getMessage(), null);
        }
    }
}
