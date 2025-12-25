package com.nhohantu.tcbookbe.cms.dto.response;

import com.nhohantu.tcbookbe.common.model.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CmsOrderResponse {
    private Long orderId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<CmsOrderDetailResponse> orderDetails;
}