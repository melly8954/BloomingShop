package com.melly.bloomingshop.restcontroller;

import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Cart;
import com.melly.bloomingshop.dto.AddToCartRequest;
import com.melly.bloomingshop.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartRestController implements ResponseController {
    private final CartService cartService;

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
}
