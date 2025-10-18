package com.nhohantu.tcbookbe.cms.dto.request;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
<<<<<<< HEAD
import lombok.Data;

@Data
public class CmsCreateCategoryRequest {
    private Long id;
    private String name;
    private CategoryModel parentCategory;
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsCreateCategoryRequest {
    private String name;
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
    private Long parentId;
    private Integer categoryLevel;
}
