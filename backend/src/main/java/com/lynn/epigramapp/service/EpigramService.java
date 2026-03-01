package com.lynn.epigramapp.service;

import com.lynn.epigramapp.exception.MissingAuthorException;
import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.model.User;
import com.lynn.epigramapp.repository.EpigramRepository;
import org.springframework.stereotype.Service;

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
    private final EpigramRepository repository;
    private final Random random = new Random();

    public EpigramService(EpigramRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns all Epigram objects stored in the database.
     *
     * @return List of Epigram objects
     */
    public List<Epigram> getAll() {
        return repository.findAll();
    }

    /**
     * Returns the Epigram with the given id.
     *
     * @param id the ID of the Epigram
     * @return Optional containing the Epigram with the given id if found, empty otherwise
     */
    public Optional<Epigram> getById(Long id) {
        return repository.findById(id);
    }

    /**
     * Returns a random Epigram from the database.
     *
     * @return Optional containing a random Epigram from the database, empty otherwise
     */
    public Optional<Epigram> getRandom() {
        List<Epigram> all = repository.findAll();
        if (all.isEmpty()) return Optional.empty();
        return Optional.of(all.get(random.nextInt(all.size())));
    }

    /**
     * Saves an Epigram object to the database.
     *
     * @param epigram the Epigram to be stored
     *
     * @return Epigram object that was stored
     */
    public Epigram save(Epigram epigram, User user) {
        if (epigram.isMine()) {
            epigram.setAuthor(user.getUsername());
        } else {
            if (epigram.getAuthor().isEmpty()) {
                throw new MissingAuthorException();
            }
        }
        return repository.save(epigram);
    }
}
