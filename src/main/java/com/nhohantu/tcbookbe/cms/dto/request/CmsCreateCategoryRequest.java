package com.nhohantu.tcbookbe.cms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsCreateCategoryRequest {
    private String name;
    private Long parentId;
    private Integer categoryLevel;
}
