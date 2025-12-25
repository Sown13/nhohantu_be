package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseOrderRepository extends JpaRepository<OrderModel, Long> {
    @EntityGraph(attributePaths = {
            "user",
            "orderDetails",
            "orderDetails.product"
    })
    Optional<OrderModel> findDetailById(Long id);
}
