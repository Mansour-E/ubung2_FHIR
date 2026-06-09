package de.medipolis.pdfparser.controller;

import de.medipolis.pdfparser.model.Dtos.FehlerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * ============================================================
 * AUFGABE 3: GlobalExceptionHandler implementieren
 * ============================================================
 *
 * Faenge folgende Exceptions ab:
 *
 * HANDLER 1: MethodArgumentNotValidException
 *   → @Valid schlaegt fehl (z.B. leerer PDF-Inhalt)
 *   → HTTP 400 Bad Request
 *   → FehlerDto: correlationId=null, fehlerCode="VALIDIERUNG_FEHLER"
 *   → Alle Feldnamen + Fehlermeldungen zusammenfassen
 *     Tipp: ex.getBindingResult().getFieldErrors().stream()...
 *
 * HANDLER 2: PdfParseException
 *   → PDF unlesbar oder Pflichtfeld fehlt
 *   → HTTP 400 Bad Request
 *   → FehlerDto mit ex.getCorrelationId() und ex.getMessage()
 *   → log.warn() mit correlationId
 *
 * HANDLER 3: Exception (catch-all)
 *   → HTTP 500 Internal Server Error
 *   → FehlerDto: correlationId=null, generische Nachricht
 *   → log.error("Unerwarteter Fehler: ", ex) — Stack Trace im Log!
 *   → NIEMALS ex.getMessage() in die Antwort!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // TODO: Handler 1 — MethodArgumentNotValidException → 400

    // TODO: Handler 2 — PdfParseException → 400

    // TODO: Handler 3 — Exception → 500 (kein Stack Trace nach aussen!)

}
