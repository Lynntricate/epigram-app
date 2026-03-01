package com.lynn.epigramapp.service;

import com.lynn.epigramapp.dto.EpigramDTO;
import com.lynn.epigramapp.exception.MissingAuthorException;
import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.model.User;
import com.lynn.epigramapp.repository.EpigramRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service layer that handles logic related to Epigrams.
 * <p>
 * This service is responsible for:
 *  - Retrieving all Epigrams
 *  - Retrieving an Epigram by id
 *  - Retrieving a random epigram
 *  - Saving a new Epigram
 */

@Service
public class EpigramService {
    private final EpigramRepository epigramRepository;
    private final Random random = new Random();
    private final UserService userService;

    public EpigramService(EpigramRepository epigramRepository, UserService userService) {
        this.epigramRepository = epigramRepository;
        this.userService = userService;
    }

    /**
     * Returns all Epigram objects stored in the database.
     *
     * @return List of Epigram objects
     */
    public List<Epigram> findAll() {
        return epigramRepository.findAll();
    }

    /**
     * Returns the Epigram with the given id.
     *
     * @param id the ID of the Epigram
     * @return Optional containing the Epigram with the given id if found, empty otherwise
     */
    public Optional<Epigram> getById(Long id) {
        return epigramRepository.findById(id);
    }

    /**
     * Returns a random Epigram from the database.
     *
     * @return Optional containing a random Epigram from the database, empty otherwise
     */
    public Optional<Epigram> getRandom() {
        List<Epigram> all = epigramRepository.findAll();
        if (all.isEmpty()) return Optional.empty();
        return Optional.of(all.get(random.nextInt(all.size())));
    }


    /**
     * Saves an Epigram object to the database.
     *
     * @param dto the DTO representing the Epigram to be saved
     *
     * @return Epigram object that was stored
     */
    public Epigram store(EpigramDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;

        String username = auth.getName(); // Username from JWT
        User user = userService.findByUsername(username);

        Epigram epigram = new Epigram();

        // Conditionally set the author of an epigram to the username if mine is true,
        // or to the custom specified author name otherwise
        if (dto.mine()) {
            epigram.setAuthor(username);
        } else {
            if (dto.author() == null || dto.author().isEmpty()) {
                // No custom author and also not marked as "mine" by user: illegal
                throw new MissingAuthorException();
            }
        }

        epigram.setContent(dto.content());
        epigram.setMine(dto.mine());
        epigram.setUser(user);

        return epigramRepository.save(epigram);

    }
}
