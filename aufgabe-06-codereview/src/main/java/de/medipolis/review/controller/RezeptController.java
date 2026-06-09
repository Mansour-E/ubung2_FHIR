package de.medipolis.review.controller;

import de.medipolis.review.model.Rezept;
import de.medipolis.review.service.RezeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller fuer Rezept-Verwaltung.
 * Ebenfalls Teil des Pull Requests von Jonas.
 * Auch hier gibt es Probleme — schau genau hin!
 */
@RestController
@RequestMapping("/api/v1/rezepte")
public class RezeptController {

    private static final Logger log = LoggerFactory.getLogger(RezeptController.class);

    private final RezeptService rezeptService;

    public RezeptController(RezeptService rezeptService) {
        this.rezeptService = rezeptService;
    }

    @PostMapping
    public ResponseEntity<?> erstelleRezept(
            @RequestParam String patientId,
            @RequestParam String patientName,
            @RequestParam String medikament,
            @RequestParam BigDecimal dosierungMg,
            @RequestParam String arztName) {

        // ❌ REVIEW-PROBLEM #9 [blocking] — Kein @Valid, keine Eingabevalidierung
        // Was passiert wenn jemand einen leeren String oder null schickt?
        // Wie sollte das korrekt geloest werden?

        Rezept rezept = rezeptService.speichereRezept(
                patientId, patientName, medikament, dosierungMg, arztName);

        return ResponseEntity.ok(rezept);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> holeRezept(@PathVariable Long id) {
        Rezept rezept = rezeptService.holeRezept(id);

        // ❌ REVIEW-PROBLEM #10 [blocking] — Kein 404 wenn nicht gefunden
        // Was passiert wenn rezept null ist?
        // Was gibt dieser Code dann zurueck?
        return ResponseEntity.ok(rezept);
    }
}
