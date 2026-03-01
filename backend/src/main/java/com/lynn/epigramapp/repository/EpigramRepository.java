package com.lynn.epigramapp.repository;

import com.lynn.epigramapp.model.Epigram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpigramRepository extends JpaRepository<Epigram, Long> { }
