package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.system.UserSysRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseUserSysRoleRepository extends JpaRepository<UserSysRoleModel, Long> {
    @Query("""
                select distinct r.roleName
                from UserSysRoleModel ur
                join ur.user u
                join ur.role r
                where u.username = :username
            """)
    List<String> findRoleNamesByUsername(@Param("username") String username);

    @Query("""
                select distinct p.permissionName
                from UserSysRoleModel ur
                join ur.user u
                join ur.role r
                join r.rolePermissions rp
                join rp.permission p
                where u.username = :username
            """)
    List<String> findPermissionNamesByUsername(@Param("username") String username);
}
