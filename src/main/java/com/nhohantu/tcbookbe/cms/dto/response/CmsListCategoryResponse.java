package com.nhohantu.tcbookbe.cms.dto.response;

import java.util.List;

public class CmsListCategoryResponse {
    private Long id;
    private String name;
    private Long parentId;
    private Integer categoryLevel;
    private List<CmsListCategoryResponse> children;
}
