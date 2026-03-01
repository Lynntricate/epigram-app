package com.lynn.epigramapp.controller;

import com.lynn.epigramapp.config.SecurityConfig;
import com.lynn.epigramapp.dto.LoginDTO;
import com.lynn.epigramapp.dto.LoginResponse;
import com.lynn.epigramapp.dto.RegisterDTO;
import com.lynn.epigramapp.exception.InvalidPasswordException;
import com.lynn.epigramapp.model.User;
import com.lynn.epigramapp.service.JwtService;
import com.lynn.epigramapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // Allow cross-origin for Svelte
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public final UserService userService;
    public final PasswordEncoder passwordEncoder;
    private final SecurityConfig securityConfig;
    private final JwtService jwtService;


    public AuthController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, SecurityConfig securityConfig) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityConfig = securityConfig;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.username());

        if (dto.password().length() < 8) {
            throw new InvalidPasswordException("Length should be >= 8");
        }

        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = userService.save(user);

        String token = jwtService.generateToken(savedUser);

        return new LoginResponse(token, user.getUsername());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDTO dto) {
        User user = userService.authenticate(dto.username(), dto.password());

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getUsername());
    }
}
