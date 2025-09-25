package com.nhohantu.tcbookbe.cms.repository;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.repository.BaseCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICmsCategoryRepository extends BaseCategoryRepository {
    List<CategoryModel> findByCategoryLevel(Integer categoryLevel);
    List<CategoryModel> findAll();
}
