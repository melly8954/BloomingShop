package com.melly.bloomingshop.admin.restcontroller;

import com.melly.bloomingshop.admin.dto.ProductManageRequest;
import com.melly.bloomingshop.admin.service.ProductManageService;
import com.melly.bloomingshop.common.ResponseController;
import com.melly.bloomingshop.common.ResponseDto;
import com.melly.bloomingshop.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/product")
public class ProductManageController implements ResponseController {
    private final ProductManageService productManageService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addProduct(@Validated @RequestBody ProductManageRequest productManageRequest, BindingResult bindingResult) {
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
            Product product = productManageService.registerProduct(productManageRequest);
            return makeResponseEntity(HttpStatus.OK,"상품 등록 성공",product);
        }catch (Exception ex){
            log.error(ex.getMessage());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러 :" + ex.getMessage(), null);
        }

    }
}
