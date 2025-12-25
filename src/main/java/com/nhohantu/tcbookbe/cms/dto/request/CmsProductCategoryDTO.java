package com.nhohantu.tcbookbe.cms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmsProductCategoryDTO {
    private Long productId;
    private Long categoryId;
}
