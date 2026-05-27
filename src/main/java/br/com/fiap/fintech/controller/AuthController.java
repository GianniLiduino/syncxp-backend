package br.com.fiap.fintech.controller;

import br.com.fiap.fintech.dto.LoginRequestDTO;
import br.com.fiap.fintech.dto.LoginResponseDTO;
import br.com.fiap.fintech.dto.RegisterRequestDTO;
import br.com.fiap.fintech.model.User;
import br.com.fiap.fintech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        try {
            return ResponseEntity.ok(authService.login(loginRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
