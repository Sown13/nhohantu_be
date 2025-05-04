package com.nhohantu.tcbookbe.service;

import com.nhohantu.tcbookbe.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.model.builder.ResponseDto;
import com.nhohantu.tcbookbe.model.dto.request.RegisterRequest;
import com.nhohantu.tcbookbe.model.dto.response.RegisterResponse;
import com.nhohantu.tcbookbe.model.entity.UserBasicInfoModel;
import com.nhohantu.tcbookbe.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.security.AuthenticationService;
import com.nhohantu.tcbookbe.security.UserDetailsImpl;
import com.nhohantu.tcbookbe.security.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@CustomLog
@RequiredArgsConstructor
public class UserBasicInfoService {
    private final AuthenticationService authService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ModelMapper mapper;

    public UserBasicInfoModel getUserInfoFromContext() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return userDetails.getUser();
        } catch (Exception e) {
            log.error("Failed when get user from context");

            return null;
        }
    }

    public ResponseEntity<ResponseDto<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        UserDetailsImpl registeredUser;
        try {
            UserDetails usernameExist = userDetailsService.loadUserByUsername(request.getUsername());
            if (usernameExist != null) {
                return ResponseBuilder.badRequestResponse("Create new user failed, please use other username",
                        StatusCodeEnum.ERRORCODE4000);
            }

            UserBasicInfoModel userBasicInfoModel;
            try {
                userBasicInfoModel = getUserInfoFromContext();
                if (userBasicInfoModel == null) {
                    return ResponseBuilder.badRequestResponse("Create new user failed, can not get user info from context",
                            StatusCodeEnum.ERRORCODE4000);
                }
            } catch (Exception e) {
                return ResponseBuilder.badRequestResponse("Create new user failed, error when get user info from context",
                        StatusCodeEnum.ERRORCODE4000);
            }

            registeredUser = authService.signup(request, userBasicInfoModel);
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse("Create new user failed, unexpected error",
                    StatusCodeEnum.ERRORCODE4000);
        }

        RegisterResponse response = mapper.map(registeredUser.getUser(), RegisterResponse.class);

        return ResponseBuilder.okResponse("Create new user successfully",
                response,
                StatusCodeEnum.SUCCESS2000);
    }
}
