package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import com.nhohantu.tcbookbe.common.repository.BaseOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends BaseOrderRepository {
    List<OrderModel> findByUserId(Long userId);
}
