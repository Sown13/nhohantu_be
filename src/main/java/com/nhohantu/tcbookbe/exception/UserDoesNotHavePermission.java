package com.nhohantu.tcbookbe.exception;

public class UserDoesNotHavePermission extends RuntimeException {

    public UserDoesNotHavePermission(String message) {
        super(message);
    }
}
