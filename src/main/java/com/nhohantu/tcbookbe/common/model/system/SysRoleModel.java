package com.nhohantu.tcbookbe.common.model.system;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sys_role")
@Entity
public class SysRoleModel extends BaseModel {
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_level")
    private String roleLevel;

    @Column(name = "role_description")
    private String roleDescription;

    @Column(name = "role_type")
    private String roleType;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermissionModel> rolePermissions = new HashSet<>();
}
