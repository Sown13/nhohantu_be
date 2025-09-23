package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.system.SysRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseSysRoleRepository extends JpaRepository<SysRoleModel, Long> {
    Optional<SysRoleModel> findByRoleName(String roleName);
}