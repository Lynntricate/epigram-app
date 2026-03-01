package com.lynn.epigramapp.controller;

import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.service.EpigramService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // Allow cross-origin for Svelte
@RestController
@RequestMapping("/api/epigram")
public class EpigramController {
    public final EpigramService service;

    public EpigramController(EpigramService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Epigram> getRandom() {
        return service.getRandom()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Epigram> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
