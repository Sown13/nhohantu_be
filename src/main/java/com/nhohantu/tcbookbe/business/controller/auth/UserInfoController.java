package com.nhohantu.tcbookbe.business.controller.auth;

import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.dto.request.RegisterRequest;
import com.nhohantu.tcbookbe.common.model.dto.response.RegisterResponse;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-info")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserBasicInfoService userCmsInfoService;

    @Operation(summary = "Sign up new user",
            description = "Registry"
    )
    @PostMapping("/create-user")
    public ResponseEntity<ResponseDTO<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        return userCmsInfoService.register(request);
    }
}