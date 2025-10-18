package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.CartModel;
<<<<<<< HEAD
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
=======
import com.nhohantu.tcbookbe.common.repository.BaseCartRepository;
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
<<<<<<< HEAD
public interface ICartRepository extends JpaRepository<CartModel, Long> {
    Optional<CartModel> findByUser (UserBasicInfoModel user);
=======
public interface ICartRepository extends BaseCartRepository {
    Optional<CartModel> findCartModelByUserId(Long userId);
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
}
