package com.nhohantu.tcbookbe.cms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CmsCreateCategoryResponse {
    private Long id;
    private String name;
//    private CategoryModel parentCategory;
//    private List<CategoryModel> childCategory;
    private Long parentId;
    private Integer categoryLevel;
    private String slug;
}
