package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCategoryRepository extends JpaRepository<CategoryModel, Long> {
}
