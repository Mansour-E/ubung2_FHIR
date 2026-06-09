package de.medipolis.support.controller;

import de.medipolis.support.model.SupportModels.*;
import de.medipolis.support.service.DiagnoseService;
import de.medipolis.support.service.LogSpeicherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * AUFGABE 2: SupportController implementieren
 * ============================================================
 *
 * Das ist dein 2nd-Level-Support-Dashboard als REST-API.
 * In Produktion wuerde hier eine Kibana-Oberflaeche sitzen.
 *
 * ENDPUNKT 1: GET /api/v1/support/logs/{correlationId}
 * --------------------------------------------------------
 * Logs fuer eine Correlation-ID holen.
 * Wie "Filter by correlationId" in Kibana.
 *
 * Schritt 1: logSpeicherService.sucheNachCorrelationId() aufrufen
 * Schritt 2: log.info() mit correlationId (Support-Techniker tracken!)
 * Schritt 3: 200 OK + Liste der Log-Eintraege
 * Schritt 4: Wenn Liste leer → 404 Not Found
 *
 * ENDPUNKT 2: GET /api/v1/support/diagnose/{correlationId}
 * --------------------------------------------------------
 * Automatische Diagnose fuer einen fehlerhaften Request.
 *
 * Schritt 1: diagnoseService.analysiereCorrelationId() aufrufen
 * Schritt 2: 200 OK + LogAnalyseErgebnis
 *
 * ENDPUNKT 3: GET /api/v1/support/fehler/letzte/{minuten}
 * --------------------------------------------------------
 * Alle Fehler der letzten N Minuten — wie Kibana Alert.
 *
 * Schritt 1: logSpeicherService.holeFehlerLetzteMinuten() aufrufen
 * Schritt 2: 200 OK + Liste der Fehler
 *
 * ENDPUNKT 4: GET /api/v1/support/status/{serviceName}
 * --------------------------------------------------------
 * System-Status eines Service abfragen.
 *
 * Schritt 1: diagnoseService.holeSystemStatus() aufrufen
 * Schritt 2: 200 OK + SystemStatus
 *
 * ENDPUNKT 5: GET /api/v1/support/fehler/uebersicht
 * --------------------------------------------------------
 * Fehler pro Service in der letzten Stunde.
 * Wie ein Kibana-Dashboard mit Fehler-Aggregation.
 *
 * Schritt 1: logSpeicherService.fehlerProService() aufrufen
 * Schritt 2: 200 OK + Map<ServiceName, AnzahlFehler>
 *
 * ENDPUNKT 6: POST /api/v1/support/ticket/{correlationId}
 * --------------------------------------------------------
 * Support-Ticket erstellen nach der Diagnose.
 *
 * Schritt 1: diagnoseService.analysiereCorrelationId() aufrufen
 * Schritt 2: Aus dem Ergebnis ein DiagnoseErgebnis bauen
 * Schritt 3: diagnoseService.erstelleSupportTicket() aufrufen
 * Schritt 4: 201 Created + SupportTicket
 */
@RestController
@RequestMapping("/api/v1/support")
public class SupportController {

    private static final Logger log = LoggerFactory.getLogger(SupportController.class);

    private final LogSpeicherService logSpeicherService;
    private final DiagnoseService diagnoseService;

    public SupportController(LogSpeicherService logSpeicherService,
                              DiagnoseService diagnoseService) {
        this.logSpeicherService = logSpeicherService;
        this.diagnoseService = diagnoseService;
    }

    @GetMapping("/logs/{correlationId}")
    public ResponseEntity<?> holeLogs(@PathVariable String correlationId) {
        // TODO: Endpunkt 1 implementieren
        throw new UnsupportedOperationException("AUFGABE 2: Implementiere holeLogs()!");
    }

    @GetMapping("/diagnose/{correlationId}")
    public ResponseEntity<?> analysiere(@PathVariable String correlationId) {
        // TODO: Endpunkt 2 implementieren
        throw new UnsupportedOperationException("AUFGABE 2: Implementiere analysiere()!");
    }

    @GetMapping("/fehler/letzte/{minuten}")
    public ResponseEntity<?> fehlerLetzteMinuten(@PathVariable int minuten) {
        // TODO: Endpunkt 3 implementieren
        throw new UnsupportedOperationException("AUFGABE 2: Implementiere fehlerLetzteMinuten()!");
    }

    @GetMapping("/status/{serviceName}")
    public ResponseEntity<?> systemStatus(@PathVariable String serviceName) {
        // TODO: Endpunkt 4 implementieren
        throw new UnsupportedOperationException("AUFGABE 2: Implementiere systemStatus()!");
    }

    @GetMapping("/fehler/uebersicht")
    public ResponseEntity<?> fehlerUebersicht() {
        // TODO: Endpunkt 5 implementieren
        throw new UnsupportedOperationException("AUFGABE 2: Implementiere fehlerUebersicht()!");
    }

    @PostMapping("/ticket/{correlationId}")
    public ResponseEntity<?> erstelleTicket(@PathVariable String correlationId) {
        // TODO: Endpunkt 6 implementieren
        throw new UnsupportedOperationException("AUFGABE 2: Implementiere erstelleTicket()!");
    }
}
