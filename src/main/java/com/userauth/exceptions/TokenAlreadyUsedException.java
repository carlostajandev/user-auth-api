package com.userauth.exceptions;

public class TokenAlreadyUsedException extends Throwable {
    public TokenAlreadyUsedException(String message) {
        super(message);
    }
}
