package com.nhohantu.tcbookbe.cms.repository;

import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICmsOrderRepository extends JpaRepository<OrderModel, Long> {
}
