package com.openpolicy.core.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

//Lo que se responde con el token

@Data
@NoArgsConstructor
@AllArgsConstructor // Genera un constructor con argumentos: new AuthResponse(token)
public class AuthResponse {
    private String token;
}