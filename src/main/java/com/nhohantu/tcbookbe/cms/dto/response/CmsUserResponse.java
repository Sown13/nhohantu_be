package com.nhohantu.tcbookbe.cms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmsUserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String primaryPhone;
    private String email;
    private String address;
    private Boolean locked;
}
