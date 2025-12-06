package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.repository.BaseCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends BaseCategoryRepository {
    List<CategoryModel> findByParentCategory_Id(Long parentId); // lấy danh mục con
}
