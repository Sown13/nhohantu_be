package com.nhohantu.tcbookbe.controller.auth;

import com.nhohantu.tcbookbe.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.model.dto.request.LoginRequest;
import com.nhohantu.tcbookbe.model.dto.response.LoginResponse;
import com.nhohantu.tcbookbe.security.AuthenticationService;
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
