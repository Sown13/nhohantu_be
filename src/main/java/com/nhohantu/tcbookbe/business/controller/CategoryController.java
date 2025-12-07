package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.response.GetProductListResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsListCategoryResponse;
import com.nhohantu.tcbookbe.cms.service.CmsCategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getProductsByCategory(
            @PathVariable String slug,
            @RequestParam(required = false) String sort_by,
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max,
            @RequestParam(required = false) Boolean on_sale
    ) {
        return categoryService.getProductsByCategorySlug(slug, sort_by, min, max, on_sale);
    }
}
