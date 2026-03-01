package com.lynn.epigramapp.controller;

import com.lynn.epigramapp.dto.EpigramDTO;
import com.lynn.epigramapp.exception.MissingAuthorException;
import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.model.User;
import com.lynn.epigramapp.service.EpigramService;
import com.lynn.epigramapp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // Allow cross-origin for Svelte
@RestController
@RequestMapping("/api/epigrams")
public class EpigramsController {
    private final EpigramService epigramService;
    private final UserService userService;

    public EpigramsController(EpigramService epigramService, UserService userService) {
        this.epigramService = epigramService;
        this.userService = userService;
    }

    @GetMapping
    public List<Epigram> all() {
        return epigramService.getAll();
    }

    @PostMapping
    public Epigram create(@RequestBody EpigramDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;

        String username = auth.getName();  // username from JWT
        User user = userService.findByUsername(username);

        Epigram epigram = new Epigram();
        epigram.setContent(dto.content());
        epigram.setAuthor(dto.author());
        epigram.setMine(dto.mine());
        epigram.setUser(user);
        return epigramService.save(epigram, user);
    }


}
