package com.userauth.exceptions;

public class TokenExpiredException extends Throwable {

    public TokenExpiredException(String message) {
        super(message);
    }
}
