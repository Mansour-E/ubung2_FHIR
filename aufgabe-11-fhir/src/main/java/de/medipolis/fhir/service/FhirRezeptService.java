package de.medipolis.fhir.service;

import de.medipolis.fhir.model.Rezept;
import de.medipolis.fhir.repository.RezeptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Geschaeftslogik fuer FHIR Rezepte.
 * Bereits fertig implementiert.
 * Deine Aufgabe: Mapper und Controller implementieren.
 */
@Service
public class FhirRezeptService {

    private final RezeptRepository repository;

    public FhirRezeptService(RezeptRepository repository) {
        this.repository = repository;
    }

    public Rezept speichereRezept(Rezept rezept) {
        rezept.setErstelltAm(LocalDateTime.now());
        return repository.save(rezept);
    }

    public Optional<Rezept> holeRezept(Long id) {
        return repository.findById(id);
    }

    public List<Rezept> alleRezepte() {
        return repository.findAll();
    }
}
