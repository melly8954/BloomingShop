package com.melly.bloomingshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVerificationRequest {
    private String mail;
    private int code;
}
