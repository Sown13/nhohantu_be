package com.nhohantu.tcbookbe.cms.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data

public class CmsCreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Boolean active;
    private String mainImageUrl;
    private List<Long> categoryIds; // bắt buộc phải có ít nhất 1

    public CmsCreateProductRequest() {
    }


    //todo cần xử lý upload và lưu ảnh
}