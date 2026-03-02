package com.lynn.epigramapp.controller;

import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.service.EpigramService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EpigramController.class)
@AutoConfigureMockMvc(addFilters = false)
class EpigramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EpigramService service;

    @Test
    void getRandomReturnsEpigramIfPresent() throws Exception {
        Epigram epigram = new Epigram();
        epigram.setContent("Hello world");
        epigram.setAuthor("Juno");
        epigram.setMine(true);

        when(service.getRandom()).thenReturn(Optional.of(epigram));

        mockMvc.perform(get("/api/epigram")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Hello world"))
                .andExpect(jsonPath("$.author").value("Juno"))
                .andExpect(jsonPath("$.mine").value(true));
    }

    @Test
    void getRandomReturnsEmptyIfNotPresent() throws Exception {
        when(service.getRandom()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/epigram"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByIdReturnsEpigramWhenPresent() throws Exception {
        Epigram epigram = new Epigram();
        epigram.setContent("Test epigram");
        epigram.setAuthor("Ashe");
        epigram.setMine(false);

        when(service.getById(1L)).thenReturn(Optional.of(epigram));

        mockMvc.perform(get("/api/epigram/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test epigram"))
                .andExpect(jsonPath("$.author").value("Ashe"))
                .andExpect(jsonPath("$.mine").value(false));
    }

    @Test
    void getByIdReturnsNoContentWhenNotFound() throws Exception {
        when(service.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/epigram/1"))
                .andExpect(status().isNoContent());
    }
}