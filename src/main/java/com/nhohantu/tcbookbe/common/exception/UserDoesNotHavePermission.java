package com.nhohantu.tcbookbe.common.exception;

public class UserDoesNotHavePermission extends RuntimeException {

    public UserDoesNotHavePermission(String message) {
        super(message);
    }
}
