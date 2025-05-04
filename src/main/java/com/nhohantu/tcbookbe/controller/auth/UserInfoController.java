package com.nhohantu.tcbookbe.controller.auth;

import com.nhohantu.tcbookbe.model.builder.ResponseDto;
import com.nhohantu.tcbookbe.model.dto.request.RegisterRequest;
import com.nhohantu.tcbookbe.model.dto.response.RegisterResponse;
import com.nhohantu.tcbookbe.service.UserBasicInfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ResponseDto<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        return userCmsInfoService.register(request);
    }
}