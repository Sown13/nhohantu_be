package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<OrderModel,Long> {
=======
import com.nhohantu.tcbookbe.common.repository.BaseOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends BaseOrderRepository {
    List<OrderModel> findByUserId(Long userId);
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
}
