package com.nhohantu.tcbookbe.common.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;

    private long expiresIn;

    private String username;

    private String email;

    private String first_name;

    private String last_name;
}
