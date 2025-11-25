package com.nhohantu.tcbookbe.cms.repository;

import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsUserBasicInfoRepository extends JpaRepository <UserBasicInfoModel, Long> {
}
