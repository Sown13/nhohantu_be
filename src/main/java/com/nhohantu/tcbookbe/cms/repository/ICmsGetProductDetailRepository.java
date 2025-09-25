package com.nhohantu.tcbookbe.cms.repository;

import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICmsGetProductDetailRepository extends JpaRepository<ProductModel,Long> {
}
