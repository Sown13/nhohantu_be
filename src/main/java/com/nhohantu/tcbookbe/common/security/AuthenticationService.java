package com.nhohantu.tcbookbe.common.security;

import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.dto.request.LoginRequest;
import com.nhohantu.tcbookbe.common.model.dto.request.RegisterRequest;
import com.nhohantu.tcbookbe.common.model.dto.response.LoginResponse;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.repository.BaseUserInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final BaseUserInfoRepo userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserDetailsImpl signup(RegisterRequest request, UserBasicInfoModel userBasicInfoModel) {
        UserBasicInfoModel user = UserBasicInfoModel.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
//                .role(RoleEnum.valueOf(request.getRole()))
                .address(request.getAddress())
                .email(request.getEmail())
                .primaryPhone(request.getPhone())
//                .createdBy(userBasicInfoModel.getId())
                .build();

        return new UserDetailsImpl(userRepository.save(user));
    }

    public UserDetailsImpl authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return new UserDetailsImpl(userRepository.findByUsername(request.getUsername())
                .orElseThrow());
    }

    public ResponseEntity<ResponseDTO<LoginResponse>> requestLogin(LoginRequest request) {
        UserDetailsImpl authenticatedUser;
        try {
            authenticatedUser = authenticate(request);
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse("Login Failed", StatusCodeEnum.ERRORCODE4000);
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseBuilder.okResponse("Login successfully", loginResponse, StatusCodeEnum.SUCCESS2000);
    }
}
