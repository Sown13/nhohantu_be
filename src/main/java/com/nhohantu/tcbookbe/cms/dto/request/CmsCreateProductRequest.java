package com.nhohantu.tcbookbe.cms.dto.request;

import com.nhohantu.tcbookbe.common.utils.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CmsCreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Integer quantity;
    private Boolean active;

    @Schema(defaultValue = Constant.DEFAULT_IMAGE_URL)
    private String mainImageUrl;

    private String videoUrl;
    private String unit;
    private String sku;
    private String brand;
    private Float weight;
    private Float discountPercentage;
    private Float rating;
    private Map<String, Object> variations;

    private List<Long> categoryIds; // bắt buộc phải có ít nhất 1
    private List<AttachmentRequest> gallery; // List ảnh upload
    private List<Long> tagIds; // tag ids nếu có
    //todo cần xử lý upload và lưu ảnh
}