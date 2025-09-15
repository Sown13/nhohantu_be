package com.nhohantu.tcbookbe.cms.dto.response;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
