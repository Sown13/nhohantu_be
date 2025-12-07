package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.response.GetProductListResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsListCategoryResponse;
import com.nhohantu.tcbookbe.cms.service.CmsCategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CmsCategoryService categoryService;

    @GetMapping("/level3")
    public ResponseEntity<ResponseDTO<List<CmsCreateCategoryResponse>>> findAllCategoryLevel3() {
        return categoryService.findAllCategoryLevel3();
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<CmsListCategoryResponse>>> findAllCategory() {
        return categoryService.findAllCategory();
    }

    @GetMapping("/category-products")
    public ResponseEntity<ResponseDTO<List<CmsListCategoryResponse>>> findCategoryProducts() {
        return categoryService.findCategoryProducts();
    }

    @GetMapping("/{slug}/products")
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getProductsByCategory(@PathVariable String slug) {
        return categoryService.getProductsByCategorySlug(slug);
    }
}
