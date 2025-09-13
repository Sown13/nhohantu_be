package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseUserInfoRepo extends JpaRepository<UserBasicInfoModel, Long> {
    Optional<UserBasicInfoModel> findByUsername(String username);
}
