package com.lynn.epigramapp.service;

import com.lynn.epigramapp.model.Epigram;
import com.lynn.epigramapp.repository.EpigramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EpigramServiceTest {

    @Mock
    EpigramRepository epigramRepository;

    @Mock
    UserService userService; // If EpigramService depends on it

    @InjectMocks
    EpigramService epigramService;

    @Test
    void returnsRandomEpigram() {
        Epigram epigram = new Epigram();
        epigram.setContent("Hello World!");
        epigram.setAuthor("Ana");

        // Set up the mock repository to return a list of just the defined epigram above
        when(epigramRepository.findAll()).thenReturn(List.of(epigram));

        // Call the service method
        Optional<Epigram> result = epigramService.getRandom();

        // Result of getRandom can now be expected, because there is only one epigram
        assertTrue(result.isPresent());
        assertEquals("Hello World!", result.get().getContent());
        assertEquals("Ana", result.get().getAuthor());

        // Verify that the repository was used
        verify(epigramRepository, times(1)).findAll();
    }

    @Test
    void returnsAllEpigrams() {
        List<Epigram> epigrams = new ArrayList<Epigram>();
        for (int i = 0; i < 5; i++) {
            Epigram epigram = new Epigram();
            epigram.setContent("Content " + i);
            epigram.setAuthor("Author " + i);
            epigrams.add(epigram);
        }

        // Set up the mock repository to return all epigrams just defined
        when(epigramRepository.findAll()).thenReturn(epigrams);

        List<Epigram> result = epigramService.findAll();


        assertEquals(5, result.size());

        for (int i = 0; i < 5; i++) {
            // Check order and presence of all epigrams
            assertEquals("Content " + i, result.get(i).getContent());
            assertEquals("Author " + i, result.get(i).getAuthor());
        }

        // Verify that the repository was used
        verify(epigramRepository, times(1)).findAll();

    }
}
