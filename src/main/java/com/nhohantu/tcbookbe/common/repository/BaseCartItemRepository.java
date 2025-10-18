package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.CartItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCartItemRepository extends JpaRepository<CartItemModel, Long> {
}
