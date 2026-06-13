package de.medipolis.pdfparser.controller;

import de.medipolis.pdfparser.model.Dtos.PdfParseRequestDto;
import de.medipolis.pdfparser.service.PdfParserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * ============================================================
 * AUFGABE 2: PdfParserController implementieren
 * ============================================================
 *
 * KONTEXT:
 * Orchestra schickt einen POST-Request mit dem PDF-Inhalt.
 * Du empfaengst ihn, rufst den PdfParserService auf,
 * und gibst das Ergebnis als JSON zurueck.
 *
 * Endpunkt: POST /api/v1/pdf-parser/parse
 *
 * ------------------------------------------------------------
 * METHODE: parsePdf()
 * ------------------------------------------------------------
 *
 * Schritt 1 — correlationId generieren:
 *   String correlationId = UUID.randomUUID().toString();
 *
 * Schritt 2 — MDC mit try-finally:
 *   MDC.put("correlationId", correlationId);
 *   try {
 *       ... gesamte Logik ...
 *   } finally {
 *       MDC.clear();
 *   }
 *
 * Schritt 3 — Logging:
 *   log.info() mit correlationId und quellSystem
 *   KEINE Patientendaten im Log! (DSGVO)
 *
 * Schritt 4 — Service aufrufen:
 *   ParseErgebnisDto result = pdfParserService.parsePdf(request, correlationId);
 *
 * Schritt 5 — Antwort:
 *   HTTP 200 OK + result
 *   Warum 200 und nicht 202?
 *   Weil dieser Service synchron antwortet — Orchestra wartet
 *   auf das Ergebnis und verarbeitet es sofort weiter.
 *
 * ------------------------------------------------------------
 * AUFGABE 3: GlobalExceptionHandler implementieren
 * ------------------------------------------------------------
 * Erstelle GlobalExceptionHandler.java im controller-Package.
 *
 * Faenge ab:
 *
 * 1. MethodArgumentNotValidException (@Valid schlaegt fehl)
 *    → 400 Bad Request
 *    → FehlerDto mit allen Validierungsfehlern zusammengefasst
 *
 * 2. PdfParseException (PDF unlesbar/unvollstaendig)
 *    → 400 Bad Request
 *    → FehlerDto mit correlationId und Fehlermeldung
 *
 * 3. Exception (catch-all)
 *    → 500 Internal Server Error
 *    → FehlerDto mit generischer Nachricht
 *    → Stack Trace NUR im Log (log.error("Unerwarteter Fehler: ", ex))
 *
 * ------------------------------------------------------------
 * Interview-Fragen:
 *
 * "Warum 200 OK hier und 202 Accepted im ersten Projekt?"
 *  → Dieser PDF-Parser ist ein synchroner Hilfsdienst.
 *    Orchestra fragt an und wartet auf die Antwort.
 *    200 = "fertig, hier ist das Ergebnis".
 *    202 = "empfangen, wird noch verarbeitet" (asynchron).
 *
 * "Warum Virtual Threads in application.yml aktiviert?"
 *  → Wenn 500 Krankenhaeuser gleichzeitig PDFs schicken,
 *    wuerden klassische Platform-Threads den Speicher
 *    aufbrauchen. Virtual Threads (Java 21) verbrauchen
 *    minimal Speicher und skalieren automatisch.
 *
 * "Was ist der Vorteil eines eigenen Microservice fuer PDF-Parsing?"
 *  → Wenn dieser Service abstuerzt, laeuft Orchestra weiter.
 *    Isolation von Fehlern. Ausserdem koennte man diesen
 *    Service unabhaengig skalieren wenn viele PDFs kommen.
 */
@RestController
@RequestMapping("/api/v1/pdf-parser")
public class PdfParserController {

    private static final Logger log = LoggerFactory.getLogger(PdfParserController.class);

    private final PdfParserService pdfParserService;

    public PdfParserController(PdfParserService pdfParserService) {
        this.pdfParserService = pdfParserService;
    }


    @PostMapping("/parse")
    public ResponseEntity<?> parsePdf(@Valid @RequestBody PdfParseRequestDto request) {

        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        try{
            log.info("PDF-Parsing Request, correlationId: {}, quellSystem: {}", correlationId, request.quellSystem());

            var result = pdfParserService.parsePdf(request, correlationId);
            return ResponseEntity.ok(result);
        }finally {
            MDC.clear();
        }


        // TODO: Implementiere den Controller
        // Schritt 1: correlationId generieren
        // Schritt 2: MDC mit try-finally
        // Schritt 3: log.info (nur quellSystem, keine Patientendaten!)
        // Schritt 4: pdfParserService.parsePdf() aufrufen
        // Schritt 5: 200 OK zurueckgeben

    }
}
