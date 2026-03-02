package com.lynn.epigramapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lynn.epigramapp.dto.LoginDTO;
import com.lynn.epigramapp.dto.LoginResponse;
import com.lynn.epigramapp.dto.RegisterDTO;
import com.lynn.epigramapp.service.JwtService;
import com.lynn.epigramapp.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void registerReturnsLoginResponse() throws Exception {
        RegisterDTO dto = new RegisterDTO("Juno", "password123");
        LoginResponse response = new LoginResponse("mocked-jwt-token", "Juno");

        when(userService.register(dto)).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.username").value("Juno"));
    }

    @Test
    void loginReturnsLoginResponse() throws Exception {
        LoginDTO dto = new LoginDTO("Juno", "password123");
        LoginResponse response = new LoginResponse("mocked-jwt-token", "Juno");

        when(userService.login(dto)).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.username").value("Juno"));
    }
}