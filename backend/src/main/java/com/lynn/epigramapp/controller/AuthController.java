package com.lynn.epigramapp.controller;

import com.lynn.epigramapp.dto.LoginDTO;
import com.lynn.epigramapp.dto.LoginResponse;
import com.lynn.epigramapp.dto.RegisterDTO;
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


    public AuthController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDTO dto) {
        return userService.login(dto);
    }
}
