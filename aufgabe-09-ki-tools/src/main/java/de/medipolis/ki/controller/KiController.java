package de.medipolis.ki.controller;

import de.medipolis.ki.model.Dtos.*;
import de.medipolis.ki.service.KiAnalyseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * ============================================================
 * AUFGABE 3: KiController implementieren
 * ============================================================
 *
 * Endpunkt: POST /api/v1/ki/arztbrief/analysiere
 *
 * Schritt 1: correlationId + MDC (try-finally!)
 * Schritt 2: log.info() — NUR quellSystem, KEIN Arztbrief-Text!
 * Schritt 3: kiAnalyseService.analysiereArztbrief() aufrufen
 * Schritt 4: 200 OK zurueckgeben
 *
 * Warum 200 und nicht 202?
 * → Orchestra wartet synchron auf das Ergebnis.
 *   200 = Analyse fertig, hier ist das Ergebnis.
 */
@RestController
@RequestMapping("/api/v1/ki")
public class KiController {

    private static final Logger log = LoggerFactory.getLogger(KiController.class);

    private final KiAnalyseService kiAnalyseService;

    public KiController(KiAnalyseService kiAnalyseService) {
        this.kiAnalyseService = kiAnalyseService;
    }

    @PostMapping("/arztbrief/analysiere")
    public ResponseEntity<?> analysiereArztbrief(
            @Valid @RequestBody ArztbriefRequestDto request) {
        // TODO: Implementiere den Controller
        throw new UnsupportedOperationException("AUFGABE 3: Implementiere analysiereArztbrief()!");
    }
}
