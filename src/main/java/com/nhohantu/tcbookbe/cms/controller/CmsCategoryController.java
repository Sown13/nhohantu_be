package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateCategoryRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.service.CmsCategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/category")
@RequiredArgsConstructor
public class CmsCategoryController {
    private final CmsCategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> createCategory(@RequestBody CmsCreateCategoryRequest request){
        return categoryService.createCategory(request);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> getCategory(@PathVariable Long id){
        return categoryService.getCategory(id);
    }
}
