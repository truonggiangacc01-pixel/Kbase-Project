package com.fpt.kbase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String fullName;
    private String systemRole;

    public JwtResponse(String accessToken, Long id, String email, String fullName, String systemRole) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.systemRole = systemRole;
    }
}
