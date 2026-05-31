package com.example.sagalobby.common.exception;

public class InvalidAuthenticationPrincipalException extends RuntimeException {
    public InvalidAuthenticationPrincipalException(String message) {
        super(message);
    }
}
