package com.nhohantu.tcbookbe.business.controller;

<<<<<<< HEAD
import com.nhohantu.tcbookbe.business.dto.CategoryDTO;
import com.nhohantu.tcbookbe.business.dto.request.CategoryRequestDTO;
import com.nhohantu.tcbookbe.business.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
=======
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsListCategoryResponse;
import com.nhohantu.tcbookbe.cms.service.CmsCategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e

import java.util.List;

@RestController
<<<<<<< HEAD
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryRequestDTO requestDTO) {
        return categoryService.createCategory(requestDTO);
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/parent/{parentId}")
    public List<CategoryDTO> getChildren(@PathVariable Long parentId) {
        return categoryService.getChildCategories(parentId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
=======
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
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
    }
}
