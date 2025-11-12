package com.nhohantu.tcbookbe.cms.dto.response;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmsCreateProductResponse {
    private Long id;
    private String name;
    private String description;
    private String coverUrl;
    private BigDecimal price;
    private Integer quantity;
    private Boolean active;
    private String mainImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CategoryModel> categories; // danh sách tên category
    private List<Long> tagIds;
}
