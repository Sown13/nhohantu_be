package com.nhohantu.tcbookbe.common.repository;

import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseUserInfoRepo extends JpaRepository<UserBasicInfoModel, Long> {
    Optional<UserBasicInfoModel> findByUsername(String username);

    @Query("""
        select distinct u
        from UserBasicInfoModel u
        left join fetch u.userRoles ur
        left join fetch ur.role r
        left join fetch r.rolePermissions rp
        left join fetch rp.permission p
        where u.username = :username
    """)
    Optional<UserBasicInfoModel> findByUsernameWithRoles(@Param("username") String username);
}
