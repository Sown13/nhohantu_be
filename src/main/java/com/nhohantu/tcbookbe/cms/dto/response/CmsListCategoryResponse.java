package com.nhohantu.tcbookbe.cms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmsListCategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String imageUrl;
    private Long parentId;
    private Integer categoryLevel;
    private Long productCount; // optional
    private List<CmsListCategoryResponse> children;
}
