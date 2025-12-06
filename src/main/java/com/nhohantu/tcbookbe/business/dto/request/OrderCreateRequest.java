package com.nhohantu.tcbookbe.business.dto.request;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {
    private List<OrderItemRequest> items;
    private BigDecimal total;
    private String paymentMethod;
    private String paymentStatus;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemRequest {
        private Long id; // productId
        private Integer quantity;
    }
}
