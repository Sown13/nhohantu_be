package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.ProductCategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseProductCategoryRepository extends JpaRepository<ProductCategoryModel, Long> {
}
