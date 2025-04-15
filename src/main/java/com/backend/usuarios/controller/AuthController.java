package com.backend.usuarios.controller;

import com.backend.usuarios.dto.LoginRequest;
import com.backend.usuarios.model.User;
import com.backend.usuarios.util.JwtUtil;
import com.backend.usuarios.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Obtener el usuario cargado desde la base de datos
            User user = (User) userDetailsService.loadUserByUsername(loginRequest.getUsername());

            // Generar token
            return jwtUtil.generateToken(user);

        } catch (AuthenticationException e) {
            return "Credenciales inválidas: " + e.getMessage();
        }
    }
}
