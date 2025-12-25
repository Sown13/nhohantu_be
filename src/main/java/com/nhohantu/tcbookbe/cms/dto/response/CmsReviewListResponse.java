package com.nhohantu.tcbookbe.cms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CmsReviewListResponse {
    private Long id;

    // product
    private Long productId;
    private String productName;
    private String productSlug;

    // author
    private String authorName;
    private String authorEmail;

    // review
    private Integer rating;
    private String title;
    private String description;

    // audit
    private LocalDateTime createdAt;
}
