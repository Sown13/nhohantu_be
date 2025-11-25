package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.request.CmsUpdateUserStatusRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsUserResponse;
import com.nhohantu.tcbookbe.cms.repository.CmsUserBasicInfoRepository;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CmsUserService {
    private final CmsUserBasicInfoRepository userRepo;

    public CmsUserService(CmsUserBasicInfoRepository userRepo) {
        this.userRepo = userRepo;
    }

    private CmsUserResponse convert(UserBasicInfoModel user) {
        CmsUserResponse res = new CmsUserResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setPrimaryPhone(user.getPrimaryPhone());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setLocked(user.isLocked());
        return res;
    }

    public List<CmsUserResponse> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public CmsUserResponse updateUserStatus(long id, CmsUpdateUserStatusRequest req) {
        UserBasicInfoModel user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        user.setLocked(req.getLocked());
        userRepo.save(user);

        return convert(user);
    }

}
