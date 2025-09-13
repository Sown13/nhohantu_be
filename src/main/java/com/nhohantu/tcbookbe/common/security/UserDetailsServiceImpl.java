package com.nhohantu.tcbookbe.common.security;

import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.repository.BaseUserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BaseUserInfoRepo userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserBasicInfoModel> userInfo = userInfoRepository.findByUsername(username);
        if (userInfo.isPresent()) {
            return new UserDetailsImpl(userInfo.get());
        }

        return null;
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Optional<UserBasicInfoModel> userBasicInfo = userInfoRepository.findById(id);
        if (userBasicInfo.isPresent()) {
            return new UserDetailsImpl(userBasicInfo.get());
        }

        return null;
    }
}
