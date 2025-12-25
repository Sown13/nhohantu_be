package com.nhohantu.tcbookbe.cms.dto.request;

import lombok.Data;

@Data
public class CmsUpdateCategoryRequest {
    private Long id;              // bắt buộc
    private String name;          // optional
    private Integer categoryLevel; // optional (nếu cho sửa level)
    private Long parentId;        // optional: null => muốn thành root (level 1)
    private String imageUrl;      // optional
}
