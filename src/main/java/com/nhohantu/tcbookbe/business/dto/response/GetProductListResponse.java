package com.nhohantu.tcbookbe.business.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetProductListResponse {
    private Long id;
    private String name;
    private String slug;
    private BigDecimal price;
    private Integer quantity;
    private Integer sold;
    private String videoUrl;
    private String unit;
    private BigDecimal salePrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String mainImageUrl;
    private AttachmentResponse mainImage;
    private String sku;
    private List<AttachmentResponse> gallery;
    private CategoryResponse category;
    private List<TagResponse> tag;
    private String brand;
    private String description;
    private Map<String, Object> variations;
    private Float rating;
    private Float discountPercentage;
    private Float weight;
}