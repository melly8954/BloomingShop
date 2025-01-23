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

import java.util.List;

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
}
