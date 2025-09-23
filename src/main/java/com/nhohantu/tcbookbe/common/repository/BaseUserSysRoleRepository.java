package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.system.UserSysRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseUserSysRoleRepository extends JpaRepository<UserSysRoleModel, Long> {
}
