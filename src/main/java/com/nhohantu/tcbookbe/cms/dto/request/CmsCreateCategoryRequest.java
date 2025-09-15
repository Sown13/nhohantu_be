package com.nhohantu.tcbookbe.cms.dto.request;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import lombok.Data;

@Data
public class CmsCreateCategoryRequest {
    private Long id;
    private String name;
    private CategoryModel parentCategory;
    private Long parentId;
    private Integer categoryLevel;
}
