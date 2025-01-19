package com.melly.bloomingshop.security.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountDeletedException extends AuthenticationException {
    public AccountDeletedException(String msg) {
        super(msg);
    }
}
