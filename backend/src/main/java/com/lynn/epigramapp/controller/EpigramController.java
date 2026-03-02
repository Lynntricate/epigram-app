package com.lynn.epigramapp.controller;

import com.lynn.epigramapp.dto.EpigramDTO;
import com.lynn.epigramapp.service.EpigramService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<EpigramDTO> getRandom() {
        return service.getRandom()
                .map(epigram -> new EpigramDTO(
                        epigram.getContent(),
                        epigram.getAuthor(),
                        epigram.isMine()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpigramDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(epigram -> new EpigramDTO(
                        epigram.getContent(),
                        epigram.getAuthor(),
                        epigram.isMine()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
