package com.melly.bloomingshop.security.exception;

import org.springframework.security.authentication.DisabledException;

public class CustomDisabledException extends DisabledException {
    public CustomDisabledException(String msg) {
        super(msg);
    }
}
