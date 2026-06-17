package de.medipolis.fhir.controller;

import ca.uhn.fhir.parser.IParser;
import de.medipolis.fhir.mapper.RezeptFhirMapper;
import de.medipolis.fhir.model.Rezept;
import de.medipolis.fhir.service.FhirRezeptService;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * FHIR REST Controller.
 *
 * Content-Type fuer FHIR: application/fhir+json
 * Das ist anders als normales application/json!
 *
 * Endpunkte (alle mit BasicAuth gesichert):
 *   POST /fhir/MedicationRequest        → Rezept speichern (nur FHIR_WRITER)
 *   GET  /fhir/MedicationRequest/{id}   → Ein Rezept als FHIR JSON
 *   GET  /fhir/MedicationRequest        → Alle als FHIR Bundle
 */
@RestController
@RequestMapping("/fhir/MedicationRequest")
public class FhirRezeptController {

    // FHIR spezifischer Content-Type — nicht application/json!
    private static final String FHIR_JSON = "application/fhir+json";

    private final FhirRezeptService service;
    private final RezeptFhirMapper mapper;
    private final IParser parser;

    public FhirRezeptController(FhirRezeptService service,
                                 RezeptFhirMapper mapper,
                                 IParser parser) {
        this.service = service;
        this.mapper = mapper;
        this.parser = parser;
    }

    /**
     * Rezept speichern und als FHIR MedicationRequest zurueckgeben.
     * Nur fuer FHIR_WRITER erlaubt (siehe SecurityConfig).
     * Bereits implementiert — als Beispiel fuer TODO 3 und 4.
     */
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = FHIR_JSON
    )
    public ResponseEntity<String> erstelleRezept(@RequestBody Rezept rezept) {
        Rezept gespeichert = service.speichereRezept(rezept);
        MedicationRequest mr = mapper.toMedicationRequest(gespeichert);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(FHIR_JSON))
            .body(parser.encodeResourceToString(mr));
    }

    /**
     * TODO 3 — Implementiere GET /{id}
     *
     * SCHRITT 1: service.holeRezept(id) aufrufen → gibt Optional<Rezept> zurueck
     *
     * SCHRITT 2: Wenn nicht gefunden → 404:
     *   if (rezeptOpt.isEmpty()) return ResponseEntity.notFound().build();
     *
     * SCHRITT 3: mapper.toMedicationRequest() aufrufen
     *
     * SCHRITT 4: parser.encodeResourceToString() zum JSON konvertieren
     *
     * SCHRITT 5: ResponseEntity mit Content-Type application/fhir+json zurueckgeben:
     *   return ResponseEntity.ok()
     *       .contentType(MediaType.parseMediaType(FHIR_JSON))
     *       .body(fhirJson);
     */
    @GetMapping(value = "/{id}", produces = FHIR_JSON)
    public ResponseEntity<String> holeRezept(@PathVariable Long id) {
        // TODO 3: Implementiere hier!
        throw new UnsupportedOperationException("TODO 3: GET /{id} implementieren!");
    }

    /**
     * TODO 4 — Implementiere GET / (alle Rezepte als Bundle)
     *
     * SCHRITT 1: service.alleRezepte() aufrufen → gibt List<Rezept> zurueck
     *
     * SCHRITT 2: mapper.toBundle() aufrufen
     *
     * SCHRITT 3: parser.encodeResourceToString() zum JSON konvertieren
     *
     * SCHRITT 4: ResponseEntity mit Content-Type application/fhir+json zurueckgeben
     */
    @GetMapping(produces = FHIR_JSON)
    public ResponseEntity<String> alleRezepte() {
        // TODO 4: Implementiere hier!
        throw new UnsupportedOperationException("TODO 4: GET / implementieren!");
    }
}
