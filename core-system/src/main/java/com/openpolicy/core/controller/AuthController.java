package com.openpolicy.core.controller;

import com.openpolicy.core.dto.AuthResponse;
import com.openpolicy.core.dto.LoginRequest;
import com.openpolicy.core.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController // <--- ¿Tienes esto?
@RequestMapping("/api/auth") // <--- ¿Dice "/api/auth"?
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login") // <--- ¿Dice "/login"? (Esto forma /api/auth/login)
    public AuthResponse login(@RequestBody LoginRequest request) {

        // Autenticar con usuario y contraseña
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Si pasa, generar token
        String token = jwtService.generateToken(request.getUsername());

        return new AuthResponse(token);
    }
}