package com.nhohantu.tcbookbe.common.model.enums;


public enum OrderStatus {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromName(String name) {
        if (name == null) {
            System.out.println("OrderStatus name is null");
            return PENDING;
        }

        return OrderStatus.valueOf(name.toUpperCase());
    }
}
