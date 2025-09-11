package com.nhohantu.tcbookbe.repository;

import com.nhohantu.tcbookbe.model.entity.system.UserBasicInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserInfoRepo extends JpaRepository<UserBasicInfoModel, Long> {
    Optional<UserBasicInfoModel> findByUsername(String username);
}
