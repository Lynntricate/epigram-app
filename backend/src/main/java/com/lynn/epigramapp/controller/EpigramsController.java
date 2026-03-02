package com.lynn.epigramapp.controller;

import com.lynn.epigramapp.dto.EpigramDTO;
import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.service.EpigramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // Allow cross-origin for Svelte
@RestController
@RequestMapping("/api/epigrams")
public class EpigramsController {
    private final EpigramService epigramService;

    public EpigramsController(EpigramService epigramService) {
        this.epigramService = epigramService;
    }

    @GetMapping("/random")
    public ResponseEntity<EpigramDTO> getRandom() {
        return epigramService.getRandom()
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
        return epigramService.getById(id)
                .map(epigram -> new EpigramDTO(
                        epigram.getContent(),
                        epigram.getAuthor(),
                        epigram.isMine()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }


    @GetMapping()
    public List<EpigramDTO> all() {
        return epigramService.findAll().stream()
                .map(e -> new EpigramDTO(
                        e.getContent(),
                        e.getAuthor(),
                        e.isMine()
                ))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EpigramDTO create(@RequestBody EpigramDTO dto) {
        Epigram saved = epigramService.store(dto);

        // Map entity to DTO
        return new EpigramDTO(
                saved.getContent(),
                saved.getAuthor(),
                saved.isMine()
        );
    }



}
