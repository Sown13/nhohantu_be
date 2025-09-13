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
@Table(name = "sys_permission")
@Entity
public class SysPermissionModel extends BaseModel {

    @Column(name = "permission_name", unique = true, nullable = false)
    private String permissionName;

    @Column(name = "permission_description")
    private String permissionDescription;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermissionModel> rolePermissions = new HashSet<>();
}