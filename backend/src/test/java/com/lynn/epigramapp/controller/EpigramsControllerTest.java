package com.lynn.epigramapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lynn.epigramapp.dto.EpigramDTO;
import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.service.EpigramService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EpigramsController.class)
@AutoConfigureMockMvc(addFilters = false)
class EpigramsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EpigramService epigramService;

    @Test
    void getAllReturnsListOfEpigrams() throws Exception {

        Epigram e1 = new Epigram();
        e1.setContent("Hello");
        e1.setAuthor("Juno");
        e1.setMine(true);

        Epigram e2 = new Epigram();
        e2.setContent("World");
        e2.setAuthor("Ashe");
        e2.setMine(false);

        when(epigramService.findAll()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/api/epigrams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("Hello"))
                .andExpect(jsonPath("$[0].author").value("Juno"))
                .andExpect(jsonPath("$[0].mine").value(true))
                .andExpect(jsonPath("$[1].content").value("World"))
                .andExpect(jsonPath("$[1].author").value("Ashe"))
                .andExpect(jsonPath("$[1].mine").value(false));
    }

    @Test
    void createReturnsCreatedEpigram() throws Exception {

        EpigramDTO request = new EpigramDTO("Test content", "Juno", true);

        Epigram saved = new Epigram();
        saved.setContent("Test content");
        saved.setAuthor("Juno");
        saved.setMine(true);

        when(epigramService.store(any(EpigramDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/epigrams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.author").value("Juno"))
                .andExpect(jsonPath("$.mine").value(true));
    }

    @Test
    void getRandomReturnsEpigramIfPresent() throws Exception {
        Epigram epigram = new Epigram();
        epigram.setContent("Hello world");
        epigram.setAuthor("Juno");
        epigram.setMine(true);

        when(epigramService.getRandom()).thenReturn(Optional.of(epigram));

        mockMvc.perform(get("/api/epigrams/random")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello world"))
                .andExpect(jsonPath("$.author").value("Juno"))
                .andExpect(jsonPath("$.mine").value(true));
    }

    @Test
    void getRandomReturnsEmptyIfNotPresent() throws Exception {
        when(epigramService.getRandom()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/epigrams/random"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByIdReturnsEpigramWhenPresent() throws Exception {
        Epigram epigram = new Epigram();
        epigram.setContent("Test epigram");
        epigram.setAuthor("Ashe");
        epigram.setMine(false);

        when(epigramService.getById(1L)).thenReturn(Optional.of(epigram));

        mockMvc.perform(get("/api/epigrams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test epigram"))
                .andExpect(jsonPath("$.author").value("Ashe"))
                .andExpect(jsonPath("$.mine").value(false));
    }

    @Test
    void getByIdReturnsNoContentWhenNotFound() throws Exception {
        when(epigramService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/epigrams/1"))
                .andExpect(status().isNoContent());
    }
}