package com.nhohantu.tcbookbe.business.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CanReviewResponse {
    private boolean canReview;
    private String reason;
    private List<ReviewableOrderModel> reviewableOrders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewableOrderModel {
        private Long orderId;
        private String orderDate;
    }
}
