package com.nhohantu.tcbookbe.business.repository;

<<<<<<< HEAD
import com.nhohantu.tcbookbe.common.model.entity.CartItem;
import com.nhohantu.tcbookbe.common.model.entity.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByCart(CartModel cart);
=======
import com.nhohantu.tcbookbe.common.repository.BaseCartItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartItemRepository extends BaseCartItemRepository {
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
}
