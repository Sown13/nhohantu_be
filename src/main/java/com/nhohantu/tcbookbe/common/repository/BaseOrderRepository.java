package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseOrderRepository extends JpaRepository<OrderModel, Long> {
}
