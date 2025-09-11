package com.nhohantu.tcbookbe.model.dto.request;

import com.nhohantu.tcbookbe.annotation.IsValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min = 6, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    @NotBlank
    @Size(min = 1, max = 255)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 255)
    private String lastName;

    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 2000)
    private String address;

    @Size(min = 9, max = 13)
    private String phone;

//    @IsValidRole
//    private String role = "BASIC_USER";
}
