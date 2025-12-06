package com.nhohantu.tcbookbe.business.repository;
import com.nhohantu.tcbookbe.common.model.entity.OrderDetailModel;
import com.nhohantu.tcbookbe.common.repository.BaseOrderDetailRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
//public interface IOrderDetailRepository extends JpaRepository<OrderDetailModel, Long> {
//

@Repository
public interface IOrderDetailRepository extends BaseOrderDetailRepository {
}
