package de.medipolis.abstimmung.controller;

import de.medipolis.abstimmung.exception.BestellungNichtGefundenException;
import de.medipolis.abstimmung.model.Dtos;
import de.medipolis.abstimmung.model.Dtos.*;
import de.medipolis.abstimmung.service.BestellungsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

/**
 * ============================================================
 * AUFGABE 2: BestellungsController implementieren
 * ============================================================
 *
 * KONTEXT:
 * Das ist der API-Contract den du mit dem Apotheker Klaus Meier
 * abgestimmt hast. Die Swagger-Annotationen (@Operation, @ApiResponse)
 * sind der "Design First" Contract — Dokumentation BEVOR Code.
 *
 * Nach Implementierung: http://localhost:8086/swagger-ui.html
 * Dort sieht Klaus genau was die API macht und kann sie testen.
 *
 * ------------------------------------------------------------
 * ENDPUNKT 1: POST /api/v1/apotheke/{apothekenId}/bestellungen
 * ------------------------------------------------------------
 * Neue Bestellung aufgeben.
 *
 * Parameter: @PathVariable apothekenId (String)
 * Body:      @Valid BestellungRequestDto
 *
 * Erfolg:    HTTP 201 Created + BestellungResponseDto
 *            Warum 201? Weil eine neue Ressource erstellt wurde.
 *
 * Fehler:    HTTP 400 (Validierungsfehler)
 *
 * Schritte:
 * 1. correlationId generieren + MDC setzen (try-finally!)
 * 2. log.info() — nur apothekenId, KEINE Medikamentendaten
 * 3. bestellungsService.erstelleBestellung() aufrufen
 * 4. ResponseEntity.status(201).body(result) zurueckgeben
 *
 * ------------------------------------------------------------
 * ENDPUNKT 2: GET /api/v1/apotheke/{apothekenId}/bestellungen/{bestellId}
 * ------------------------------------------------------------
 * Status einer Bestellung abfragen.
 *
 * Erfolg:    HTTP 200 OK + BestellungStatusDto
 * Fehler:    HTTP 404 Not Found + FehlerDto
 *
 * ------------------------------------------------------------
 * ENDPUNKT 3: GET /api/v1/apotheke/{apothekenId}/bestellungen
 * ------------------------------------------------------------
 * Alle Bestellungen einer Apotheke abfragen.
 *
 * Erfolg:    HTTP 200 OK + BestellungListeDto
 *            (leere Liste wenn keine vorhanden — KEIN 404!)
 *
 * ------------------------------------------------------------
 * ENDPUNKT 4: DELETE /api/v1/apotheke/{apothekenId}/bestellungen/{bestellId}
 * ------------------------------------------------------------
 * Bestellung stornieren.
 *
 * Erfolg:    HTTP 200 OK + BestellungResponseDto mit Status "STORNIERT"
 * Fehler:    HTTP 404 (nicht gefunden) oder 409 Conflict (schon in Bearbeitung)
 *
 * ------------------------------------------------------------
 * AUFGABE 3: GlobalExceptionHandler implementieren
 * ------------------------------------------------------------
 * Faenge ab:
 * - MethodArgumentNotValidException → 400 + FehlerDto
 *   Fehlermeldung muss fuer den Apotheker verstaendlich sein!
 *   NICHT: "dosierungMg: must be positive"
 *   SONDERN: "Die Menge muss eine positive Zahl sein."
 *
 * - BestellungNichtGefundenException → 404 + FehlerDto
 *   Nachricht: "Wir konnten keine Bestellung mit dieser ID finden."
 *
 * - Exception (catch-all) → 500 + FehlerDto
 *   Nachricht: "Ein technischer Fehler ist aufgetreten.
 *               Bitte kontaktieren Sie den IT-Support."
 *
 * ------------------------------------------------------------
 * AUFGABE 4: @Schema Annotationen in Dtos.java hinzufuegen
 * ------------------------------------------------------------
 * Gehe in Dtos.java und fuege bei BestellungRequestDto
 * fuer jedes Feld @Schema mit description und example hinzu.
 * Das ist der API-Contract fuer den Apotheker!
 */
@RestController
@RequestMapping("/api/v1/apotheke")
@Tag(name = "Apotheken-Bestellungen",
     description = "API fuer Medipolis-Apotheken zur Verwaltung von Medikamenten-Bestellungen")
public class BestellungsController {

    private static final Logger log = LoggerFactory.getLogger(BestellungsController.class);

    private final BestellungsService bestellungsService;

    public BestellungsController(BestellungsService bestellungsService) {
        this.bestellungsService = bestellungsService;
    }

    @PostMapping("/{apothekenId}/bestellungen")
    @Operation(
            summary = "Neue Bestellung aufgeben",
            description = "Gibt eine neue Medikamenten-Bestellung fuer die Apotheke auf. " +
                          "Die Bestellung wird sofort bestaetigt und eine Bestell-ID zugewiesen."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bestellung erfolgreich aufgegeben"),
            @ApiResponse(responseCode = "400", description = "Ungueltige Eingabe — Pflichtfelder fehlen oder Format falsch")
    })
    public ResponseEntity<?> erstelleBestellung(
            @Parameter(description = "ID der Apotheke", example = "APO-JENA-001")
            @PathVariable String apothekenId,
            @Valid @RequestBody BestellungRequestDto request) {

        UUID correlationID = UUID.randomUUID();


        MDC.put("correlationID", correlationID.toString());

        try{
            log.info("Erstelle Bestellung fuer apothekenId={}", apothekenId);

            BestellungResponseDto response = bestellungsService.erstelleBestellung(request,apothekenId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }finally {
            MDC.clear();
        }

        // TODO: Implementiere Endpunkt 1
        // 1. correlationId + MDC (try-finally!)
        // 2. log.info (nur apothekenId, kein Medikament!)
        // 3. Service aufrufen
        // 4. 201 Created zurueckgeben
    }

    @GetMapping("/{apothekenId}/bestellungen/{bestellId}")
    @Operation(
            summary = "Bestellungsstatus abfragen",
            description = "Gibt den aktuellen Status einer Bestellung zurueck."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status erfolgreich abgerufen"),
            @ApiResponse(responseCode = "404", description = "Bestellung nicht gefunden")
    })


    public ResponseEntity<?> holeBestellungStatus(
            @PathVariable String apothekenId,
            @PathVariable String bestellId) throws AccessDeniedException {

            BestellungStatusDto status = bestellungsService.holeBestellungStatus(bestellId);

            if (!status.apothekenId().equals(apothekenId)) {
                throw new AccessDeniedException("Sie haben keinen Zugriff auf diese Apotheke.");
            }
            return ResponseEntity.ok(status);


        // TODO: Implementiere Endpunkt 2
        // Service aufrufen, 200 OK zurueckgeben
        // BestellungNichtGefundenException → wird vom GlobalExceptionHandler abgefangen
    }


    @GetMapping("/{apothekenId}/bestellungen")
    @Operation(
            summary = "Alle Bestellungen einer Apotheke",
            description = "Gibt alle Bestellungen der Apotheke zurueck. " +
                          "Gibt eine leere Liste zurueck wenn keine Bestellungen vorhanden sind."
    )
    @ApiResponse(responseCode = "200", description = "Liste erfolgreich abgerufen")
    public ResponseEntity<?> holeAlleBestellungen(@PathVariable String apothekenId) {


        BestellungListeDto statusList = bestellungsService.holeAlleBestellungen(apothekenId);

        return ResponseEntity.ok(statusList);
        // TODO: Implementiere Endpunkt 3
        // Service aufrufen, 200 OK zurueckgeben
        // WICHTIG: Leere Liste → 200 OK, NICHT 404!

    }


    @DeleteMapping("/{apothekenId}/bestellungen/{bestellId}")
    @Operation(
            summary = "Bestellung stornieren",
            description = "Storniert eine Bestellung. " +
                          "Nur moeglich wenn die Bestellung noch nicht in Bearbeitung ist."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bestellung erfolgreich storniert"),
            @ApiResponse(responseCode = "404", description = "Bestellung nicht gefunden"),
            @ApiResponse(responseCode = "409", description = "Bestellung kann nicht mehr storniert werden")
    })
    public ResponseEntity<?> storniereBestellung(
            @PathVariable String apothekenId,
            @PathVariable String bestellId) throws AccessDeniedException {

        BestellungStatusDto status = bestellungsService.holeBestellungStatus(bestellId);

        if (!status.apothekenId().equals(apothekenId)) {
            throw new AccessDeniedException("Sie haben keinen Zugriff auf diese Apotheke.");
        }

        BestellungResponseDto response = bestellungsService.storniereBestellung(bestellId);
        return ResponseEntity.ok(response);
    }


}
