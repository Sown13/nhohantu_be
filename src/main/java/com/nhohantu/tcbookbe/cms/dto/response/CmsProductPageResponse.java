package com.nhohantu.tcbookbe.cms.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CmsProductPageResponse {
    private List<CmsCreateProductResponse> content; // danh sách sản phẩm
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}