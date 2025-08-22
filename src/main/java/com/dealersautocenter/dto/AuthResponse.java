package com.dealersautocenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    
    public AuthResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}