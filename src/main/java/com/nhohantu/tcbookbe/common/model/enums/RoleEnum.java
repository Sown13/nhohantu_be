package com.nhohantu.tcbookbe.common.model.enums;

public enum RoleEnum {
    ROOT("ROOT"),
    ADMIN("ADMIN"),
    BASIC_USER("BASIC_USER");

    public final String value;

    RoleEnum(String i) {
        value = i;
    }
}
