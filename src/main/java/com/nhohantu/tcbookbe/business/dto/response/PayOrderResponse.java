package com.nhohantu.tcbookbe.business.dto.response;

import com.nhohantu.tcbookbe.common.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderResponse {

    private Long orderId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime paidAt;

    private List<OrderItem> items;

    @Data
    @Builder
    public static class OrderItem {
        private Long productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private String productImage;
    }
}
