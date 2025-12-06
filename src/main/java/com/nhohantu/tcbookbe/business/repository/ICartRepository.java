package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.CartModel;

import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhohantu.tcbookbe.common.repository.BaseCartRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ICartRepository extends JpaRepository<CartModel, Long> {
    Optional<CartModel> findCartModelByUserId(Long userId);
}
