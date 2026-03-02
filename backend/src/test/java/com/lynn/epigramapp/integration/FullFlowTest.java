package com.lynn.epigramapp.integration;

import com.jayway.jsonpath.JsonPath;
import com.lynn.epigramapp.dto.EpigramDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FullFlowTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Test
    void registerLoginAndCreateEpigramFlow() throws Exception {
        EpigramDTO epigram = new EpigramDTO("Hello!", "Juno", true); // Sample epigram

        // Post epigram without token - should be forbidden
        mockMvc.perform(post("/api/epigrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epigram)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Register (should also directly log in)
        var loginResultRegister = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Juno\", \"password\": \"password123\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Juno"))
                .andReturn();

        // Extract JWT token from login response
        String tokenA = JsonPath.read(loginResultRegister.getResponse().getContentAsString(), "$.token");

        // Post epigram while logged in, with token
        mockMvc.perform(post("/api/epigrams")
                        .header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epigram)))
                .andDo(print())
                .andExpect(status().isCreated());

        // Login
        var loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Juno\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String tokenB = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");

        assertEquals(tokenA, tokenB); // New login, should provide the same token, claims are identical

        // Post epigram while logged in, should still work
        mockMvc.perform(post("/api/epigrams")
                        .header("Authorization", "Bearer " + tokenB)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(epigram)))
                .andDo(print())
                .andExpect(status().isCreated());

        // Retrieve epigrams and check
        mockMvc.perform(get("/api/epigrams")
                        .header("Authorization", "Bearer " + tokenB)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello!"))
                .andExpect(jsonPath("$[0].author").value("Juno"))
                .andExpect(jsonPath("$[0].mine").value(true));
    }
}