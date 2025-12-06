package com.nhohantu.tcbookbe.business.controller;


import com.nhohantu.tcbookbe.business.dto.CategoryDTO;
import com.nhohantu.tcbookbe.business.dto.request.CategoryRequestDTO;
import com.nhohantu.tcbookbe.business.service.CategoryService;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsListCategoryResponse;
import com.nhohantu.tcbookbe.cms.service.CmsCategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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
}
