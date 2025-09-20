package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.CategoryDTO;
import com.nhohantu.tcbookbe.business.dto.request.CategoryRequestDTO;
import com.nhohantu.tcbookbe.business.repository.ICategoryRepository;
import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO createCategory(CategoryRequestDTO requestDTO) {
        CategoryModel parent = null;
        if (requestDTO.getParentId() != null) {
            parent = categoryRepository.findById(requestDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        }

        CategoryModel category = CategoryModel.builder()
                .name(requestDTO.getName())
                .parentCategory(parent)
                .categoryLevel(parent == null ? 1 : parent.getCategoryLevel() + 1)
                .build();

        categoryRepository.save(category);

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public CategoryDTO getCategoryById(Long id) {
        CategoryModel category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return new CategoryDTO(category.getId(), category.getName());
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getChildCategories(Long parentId) {
        return categoryRepository.findByParentCategory_Id(parentId)
                .stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}
