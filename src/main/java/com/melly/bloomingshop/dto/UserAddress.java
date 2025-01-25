package com.melly.bloomingshop.dto;

import com.melly.bloomingshop.domain.Address;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddress {
    private String postcode;
    private String address;
    private String detailAddress;

    // 생성자
    public UserAddress(Address address) {
        this.postcode = address.getPostcode();
        this.address = address.getAddress();
        this.detailAddress = address.getDetailAddress();
    }
}
