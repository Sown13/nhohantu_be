package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateCategoryRequest;
import com.nhohantu.tcbookbe.cms.dto.request.CmsUpdateCategoryRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsListCategoryResponse;
import com.nhohantu.tcbookbe.cms.service.CmsCategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/category")
@RequiredArgsConstructor
public class CmsCategoryController {
    private final CmsCategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> createCategory(@RequestBody CmsCreateCategoryRequest request){
        return categoryService.createCategory(request);
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> getCategory(@PathVariable Long id){
//        return categoryService.getCategory(id);
//    }

    @GetMapping("/level3")
    public ResponseEntity<ResponseDTO<List<CmsCreateCategoryResponse>>> findAllCategoryLevel3() {
        return categoryService.findAllCategoryLevel3();
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<CmsListCategoryResponse>>> findAllCategory() {
        return categoryService.findAllCategory();
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> update(@RequestBody CmsUpdateCategoryRequest request) {
        return categoryService.updateCategory(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> delete(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/migrate-slug")
    public ResponseEntity<ResponseDTO<String>> migrateSlug() {
        return categoryService.migrateSlugForAllCategories();
    }
}
