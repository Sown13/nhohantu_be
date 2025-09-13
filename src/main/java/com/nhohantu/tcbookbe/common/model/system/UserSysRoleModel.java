package com.nhohantu.tcbookbe.common.model.system;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**Bảng trung gian User - SysRole*/
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_sys_role")
@Entity
public class UserSysRoleModel extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBasicInfoModel user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private SysRoleModel role;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by")
    private String assignedBy;
}