package com.nhohantu.tcbookbe.common.model.system;

import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_basic_info")
@Entity
public class UserBasicInfoModel extends BaseModel {
    @Column(name = "username", columnDefinition = "VARCHAR(50)", unique = true, nullable = false)
    private String username;

    @Column(name = "password", columnDefinition = "VARCHAR(64)")
    private String password;

    @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(255)")
    private String lastName;

    @Column(name = "phone", columnDefinition = "VARCHAR(13)")
    private String primaryPhone;

    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;

//    @Column(name = "role", columnDefinition = "VARCHAR(50)")
//    @Enumerated(EnumType.STRING)
//    private RoleEnum role;

    @Column(name = "created_by", columnDefinition = "INT")
    private Long createdBy;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserSysRoleModel> userRoles = new HashSet<>();
}
