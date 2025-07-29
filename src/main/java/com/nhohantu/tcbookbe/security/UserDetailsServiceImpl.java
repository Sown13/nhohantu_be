package com.nhohantu.tcbookbe.security;

import com.nhohantu.tcbookbe.model.entity.UserBasicInfoModel;
import com.nhohantu.tcbookbe.repository.IUserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserInfoRepo userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserBasicInfoModel> userInfo = userInfoRepository.findByUsername(username);
        if (userInfo.isPresent()) {
            return new UserDetailsImpl(userInfo.get());
        }

        return null;
    }

    public UserDetails loadUserById(Integer id) throws UsernameNotFoundException {
        Optional<UserBasicInfoModel> userBasicInfo = userInfoRepository.findById(id);
        if (userBasicInfo.isPresent()) {
            return new UserDetailsImpl(userBasicInfo.get());
        }

        return null;
    }
}
