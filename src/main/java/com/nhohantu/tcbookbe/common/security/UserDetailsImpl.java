package com.nhohantu.tcbookbe.common.security;

import com.nhohantu.tcbookbe.common.model.system.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private UserBasicInfoModel user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String ROLE_PREFIX = "ROLE_";
        String PERM_PREFIX = "PERM_";
        return user.getUserRoles().stream()
                .flatMap(userRole -> {
                    SysRoleModel role = userRole.getRole();

                    // Gộp ROLE + PERMISSION
                    Stream<GrantedAuthority> roleAuthorities = Stream.of(
                            new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())
                    );

                    Stream<GrantedAuthority> permissionAuthorities = role.getRolePermissions().stream()
                            .map(RolePermissionModel::getPermission)
                            .map(SysPermissionModel::getPermissionName)
                            .map(permName -> new SimpleGrantedAuthority(PERM_PREFIX + permName));

                    return Stream.concat(roleAuthorities, permissionAuthorities);
                })
                .collect(Collectors.toSet()); // dùng Set để tránh trùng lặp
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
