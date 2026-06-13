package de.medipolis.pdfparser.controller;

import de.medipolis.pdfparser.exception.PdfParseException;
import de.medipolis.pdfparser.model.Dtos.FehlerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FehlerDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(new FehlerDto(null, "VALIDIERUNG_FEHLER", message));
    }

    // TODO: Handler 2 — PdfParseException → 400
    @ExceptionHandler(PdfParseException.class)
    public ResponseEntity<FehlerDto> handlePdfParseException(PdfParseException ex) {

        log.warn("PDF Parse Fehler , correlationId: {}, Fehler: {}", ex.getCorrelationId(), ex.getMessage());
        return ResponseEntity.badRequest().body(new FehlerDto(ex.getCorrelationId(), "PDF_PARSE_FEHLER", ex.getMessage()));
    }
    // TODO: Handler 3 — Exception → 500 (kein Stack Trace nach aussen!)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FehlerDto> handleAllExceptions(Exception ex) {
        log.error("Unerwarteter Fehler: ", ex);
        return ResponseEntity.internalServerError().body(new FehlerDto(null, "500", "Unerwarteter Fehler"));
    }

}
