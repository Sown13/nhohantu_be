package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.CategoryDTO;
import com.nhohantu.tcbookbe.business.dto.request.CategoryRequestDTO;
import com.nhohantu.tcbookbe.business.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    }
}
