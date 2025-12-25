package com.nhohantu.tcbookbe.business.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private BigDecimal totalPrice;
    private String status;
    private UserBasicInfoSummary user;
    private List<OrderItemResponse> orderDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserBasicInfoSummary {
        private Long id;
        private String email;
        private String fullName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemResponse {
        private Long id;
        private String name;
        private BigDecimal price;
        private Integer quantity;
        private String productImage;
    }
}
