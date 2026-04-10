package com.librasja.libras_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponseDto {

    private String accessToken;
    private String tokenType;
    private Long userId;
    private String username;
    private String role;

    public JwtAuthResponseDto(String accessToken, Long userId, String username, String role) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}
