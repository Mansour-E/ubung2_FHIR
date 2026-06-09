package de.medipolis.pdfparser.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTOs als Java Records — modern, unveraenderlich, kein Boilerplate.
 *
 * HINWEIS: Diese Klasse ist fertig. Du nutzt sie in deinen Aufgaben.
 *
 * Interview-Frage: "Warum Records statt normale Klassen?"
 * Antwort: Records sind seit Java 16 der Standard fuer unveraenderliche
 *          Datenstrukturen (DTOs). Kein Getter/Setter Boilerplate,
 *          automatisches equals/hashCode/toString, kompakter Code.
 */
public class Dtos {

    /**
     * Eingehende Anfrage von Orchestra.
     * Orchestra schickt ein simuliertes PDF als Text-Inhalt
     * (in echten Projekten waere das Base64-kodiertes PDF).
     */
    public record PdfParseRequestDto(
            @NotBlank(message = "PDF-Inhalt darf nicht leer sein")
            String pdfInhalt,

            @NotBlank(message = "Quell-System muss angegeben werden")
            String quellSystem   // z.B. "Orchestra", "Krankenhaus-Jena"
    ) {}

    /**
     * Ergebnis der PDF-Verarbeitung — geht zurueck an Orchestra.
     * Orchestra leitet es dann an die Apotheke weiter.
     */
    public record ParseErgebnisDto(
            String correlationId,
            String patientId,
            String medikament,
            BigDecimal dosierungMg,
            String einheit,        // z.B. "mg", "ml"
            String status          // "ERFOLGREICH" | "FEHLERHAFT"
    ) {}

    /**
     * Fehlerpayload — niemals Stack Trace nach aussen!
     */
    public record FehlerDto(
            String correlationId,
            String fehlerCode,
            String nachricht
    ) {}
}
