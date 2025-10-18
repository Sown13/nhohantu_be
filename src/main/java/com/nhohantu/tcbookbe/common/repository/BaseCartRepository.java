package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.entity.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCartRepository extends JpaRepository<CartModel, Long> {
}
