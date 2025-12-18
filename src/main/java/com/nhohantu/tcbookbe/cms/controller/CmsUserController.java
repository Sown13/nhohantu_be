package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.CmsUpdateUserStatusRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsUserResponse;
import com.nhohantu.tcbookbe.cms.service.CmsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/user")
@RequiredArgsConstructor
public class CmsUserController {
    private final CmsUserService cmsUserService;

    @GetMapping
    public List<CmsUserResponse> getAllUsers(){
        return cmsUserService.getAllUsers();
    }

    @PutMapping("/{id}/status")
    public CmsUserResponse updateUserStatus(@PathVariable Long id,
                                            @RequestBody CmsUpdateUserStatusRequest cmsUpdateUserStatusRequest){
        return cmsUserService.updateUserStatus(id, cmsUpdateUserStatusRequest);
    }
}
