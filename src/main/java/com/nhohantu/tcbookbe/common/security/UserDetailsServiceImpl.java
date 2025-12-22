package com.nhohantu.tcbookbe.common.security;

import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.repository.BaseUserInfoRepo;
import com.nhohantu.tcbookbe.common.repository.BaseUserSysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BaseUserInfoRepo userInfoRepository;
    private final BaseUserSysRoleRepository sysRoleRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UserBasicInfoModel> userInfo = userInfoRepository.findByUsername(username);
//        if (userInfo.isPresent()) {
//            return new UserDetailsImpl(userInfo.get());
//        }
//
//        return null;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserBasicInfoModel user = userInfoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<String> roleNames = sysRoleRepository.findRoleNamesByUsername(username);
//        List<String> permNames = userInfoRepository.findPermissionNamesByUsername(username); // optional

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String rn : roleNames) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + rn));
        }
//        permNames.forEach(pn -> authorities.add(new SimpleGrantedAuthority("PERM_" + pn)));

        // distinct nhưng KHÔNG gán lại
        Collection<GrantedAuthority> distinctAuthorities =
                authorities.stream().distinct().collect(Collectors.toSet());

        return new UserDetailsImpl(user, distinctAuthorities);
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Optional<UserBasicInfoModel> userBasicInfo = userInfoRepository.findById(id);
        if (userBasicInfo.isPresent()) {
            return new UserDetailsImpl(userBasicInfo.get());
        }

        return null;
    }
}
