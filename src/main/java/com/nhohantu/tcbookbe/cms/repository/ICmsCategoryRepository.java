package com.nhohantu.tcbookbe.cms.repository;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.repository.BaseCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICmsCategoryRepository extends BaseCategoryRepository {
    List<CategoryModel> findByCategoryLevel(Integer categoryLevel);
    List<CategoryModel> findAll();
    Optional<CategoryModel> findBySlug(String slug);
    List<CategoryModel> findAllByCategoryLevel(Integer categoryLevel);
    boolean existsBySlugAndCategoryLevel(String slug, Integer categoryLevel);
}
