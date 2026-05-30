package com.colegio.bffservice.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.bffservice.dto.AuthResponse;
import com.colegio.bffservice.dto.LoginRequest;
import com.colegio.bffservice.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final Map<String, String> USERS = new ConcurrentHashMap<>();
    static {
        USERS.put("admin", "admin123");
        USERS.put("alumno", "password");
    }
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:4173")
    public ResponseEntity<String> register(@RequestBody LoginRequest loginRequest) {
        logger.info("POST /auth/register - intento de registro usuario={}", loginRequest.getUsername());
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username y password requeridos");
        }
        if (USERS.containsKey(loginRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario ya existe");
        }
        USERS.put(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4173")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("POST /auth/login - intent login usuario={}", loginRequest.getUsername());

        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String expectedPassword = USERS.get(loginRequest.getUsername());
        if (expectedPassword == null || !expectedPassword.equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
