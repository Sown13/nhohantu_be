package com.nhohantu.tcbookbe.business.controller.auth;

import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.dto.request.LoginRequest;
import com.nhohantu.tcbookbe.common.model.dto.response.LoginResponse;
import com.nhohantu.tcbookbe.common.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<ResponseDTO<LoginResponse>> authenticate(@RequestBody LoginRequest request) {
        return authenticationService.requestLogin(request);
    }
}
