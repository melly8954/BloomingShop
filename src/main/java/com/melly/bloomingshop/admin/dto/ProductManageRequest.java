package com.melly.bloomingshop.admin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductManageRequest {
    @NotBlank(message = "카테고리는 반드시 선택해야 합니다.")
    private String category;

    @NotBlank(message = "상품명은 반드시 입력해야 합니다.")
    private String name;

    @NotNull(message = "가격은 반드시 입력해야 합니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    private BigDecimal price;

    @NotBlank(message = "사이즈는 반드시 입력해야 합니다.")
    private String size;

    private String description;

}
