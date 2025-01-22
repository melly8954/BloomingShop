package com.melly.bloomingshop.admin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductModifyRequest {
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId; // 수정할 상품의 ID

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @NotNull(message = "가격은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    private BigDecimal price;

    @NotBlank(message = "사이즈는 필수입니다.")
    private String size;

    private String description; // 설명은 선택 사항
}
