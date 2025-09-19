package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderModel,Long> {
}
