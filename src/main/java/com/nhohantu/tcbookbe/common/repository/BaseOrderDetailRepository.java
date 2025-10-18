package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.OrderDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseOrderDetailRepository extends JpaRepository<OrderDetailModel, Long> {
}
