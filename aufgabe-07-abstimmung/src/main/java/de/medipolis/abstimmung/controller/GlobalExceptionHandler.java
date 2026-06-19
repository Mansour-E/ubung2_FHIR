package de.medipolis.abstimmung.controller;

import de.medipolis.abstimmung.exception.BestellungNichtGefundenException;
import de.medipolis.abstimmung.model.Dtos.FehlerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
 * BESONDERS WICHTIG in dieser Aufgabe:
 * Die Fehlermeldungen muessen fuer den APOTHEKER verstaendlich sein!
 * Klaus Meier ist kein IT-Experte — er versteht keine Java-Exceptions.
 *
 * HANDLER 1: MethodArgumentNotValidException
 *   → HTTP 400 Bad Request
 *   → FehlerDto:
 *     fehlerCode: "EINGABE_FEHLER"
 *     nachrichtFuerApotheker: verstaendliche Zusammenfassung der Fehler
 *     NICHT: "dosierungMg: must be positive"
 *     SONDERN: "Bitte pruefen Sie Ihre Eingaben: Menge muss groesser als 0 sein."
 *
 * HANDLER 2: BestellungNichtGefundenException
 *   → HTTP 404 Not Found
 *   → FehlerDto:
 *     fehlerCode: "BESTELLUNG_NICHT_GEFUNDEN"
 *     nachrichtFuerApotheker:
 *       "Wir konnten keine Bestellung mit dieser ID finden.
 *        Bitte pruefen Sie die Bestell-ID oder kontaktieren Sie den Support."
 *
 * HANDLER 3: IllegalStateException (Bestellung schon in Bearbeitung)
 *   → HTTP 409 Conflict
 *   → FehlerDto:
 *     fehlerCode: "STORNIERUNG_NICHT_MOEGLICH"
 *     nachrichtFuerApotheker: ex.getMessage() (hier ist die Message bereits klar!)
 *
 * HANDLER 4: Exception (catch-all)
 *   → HTTP 500 Internal Server Error
 *   → FehlerDto:
 *     fehlerCode: "TECHNISCHER_FEHLER"
 *     nachrichtFuerApotheker:
 *       "Ein technischer Fehler ist aufgetreten.
 *        Bitte kontaktieren Sie den IT-Support unter support@medipolis.de"
 *     technischeDetails: null (NIEMALS Stack Trace nach aussen!)
 *   → log.error("Unerwarteter Fehler: ", ex) NUR im Log!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // TODO: Handler 1 — Validierungsfehler → 400
    // Tipp: Fehlermeldungen sammeln und zusammenfassen
    // ex.getBindingResult().getFieldErrors().stream()...
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FehlerDto> handleValidationException(MethodArgumentNotValidException ex) {

        String fehlermeldung = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(","));

        FehlerDto fehler = new FehlerDto("EINGABE_FEHLER","Bitte pruefen sie Ihre Eingabe: "+ fehlermeldung , null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fehler);
    }

    // TODO: Handler 2 — BestellungNichtGefundenException → 404
    @ExceptionHandler(BestellungNichtGefundenException.class)
    public ResponseEntity<FehlerDto> handleBestellungException(BestellungNichtGefundenException ex) {

        FehlerDto fehler = new FehlerDto("BESTELLUNG_NICHT_GEFUNDEN", "Wir konnten keine Bestellung mit dieser ID finden.Bitte pruefen Sie die Bestell-ID oder kontaktieren Sie den Support." , null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fehler);

    }

    // TODO: Handler 3 — IllegalStateException → 409 Conflict
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<FehlerDto> handleIllegalState(IllegalStateException ex){

        FehlerDto fehler = new FehlerDto("STORNIERUNG_NICHT_MOEGLICH" , ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(fehler);
    }

    // TODO: Handler 4 — Exception → 500 (KEIN Stack Trace nach aussen!)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FehlerDto> handleException(Exception ex){

        log.error("Unerwarteter Fehler: ", ex);

        FehlerDto fehler = new FehlerDto("TECHNISCHER_FEHLER", "Ein technischer Fehler ist aufgetreten.Bitte kontaktieren Sie den IT-Support unter support@medipolis.de" , null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fehler);

    }
}
