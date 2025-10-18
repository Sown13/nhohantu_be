package com.nhohantu.tcbookbe.business.repository;

<<<<<<< HEAD
import com.nhohantu.tcbookbe.common.model.entity.OrderDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetailModel, Long> {
=======
import com.nhohantu.tcbookbe.common.repository.BaseOrderDetailRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends BaseOrderDetailRepository {
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
}
