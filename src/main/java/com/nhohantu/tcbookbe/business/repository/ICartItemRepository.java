package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.CartItem;
import com.nhohantu.tcbookbe.common.model.entity.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByCart(CartModel cart);
}
