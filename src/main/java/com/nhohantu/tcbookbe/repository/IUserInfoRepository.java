package com.nhohantu.tcbookbe.repository;

import com.nhohantu.tcbookbe.model.entity.UserBasicInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserInfoRepository extends JpaRepository<UserBasicInfoModel, Integer> {
    Optional<UserBasicInfoModel> findByUsername(String username);
}
