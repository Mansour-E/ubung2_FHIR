package de.medipolis.abstimmung.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTOs fuer die Apotheken-Bestellungs-API.
 *
 * Diese werden im API-Contract (Swagger) dokumentiert.
 * Jedes Record-Feld bekommt eine @Schema Beschreibung
 * damit der Apotheker versteht was er schicken muss.
 */
public class Dtos {

    /**
     * Eingehende Bestellung vom Apotheker.
     *
     * AUFGABE: Fuege @Schema Annotationen hinzu damit
     * der API-Contract klar und verstaendlich ist.
     */
    public record BestellungRequestDto(

            @NotBlank(message = "Apotheken-ID ist Pflichtfeld")
            @Schema(description = "Apotheken Id von ihnen wird benötigt " , example = "APO-JENA-001")
            String apothekenId,

            @NotBlank(message = "Medikament ist Pflichtfeld")
            @Schema(description = "medikamenten Name mit einem gültigen Zeichen " , example = "Ibuprofen")
            String medikament,

            @NotNull(message = "Menge ist Pflichtfeld")
            @Positive(message = "Menge muss groesser als 0 sein")
            @Schema(description = "Bestellte Menge des Medikaments", example = "5")
            Integer menge,

            @NotBlank(message = "Einheit ist Pflichtfeld")
            @Schema(description = "Einheit der Bestellung", example = "mg", allowableValues = {"mg", "ml", "Stueck"})
            String einheit,

            // Optionales Feld — Prioritaet der Bestellung
            @Schema(description = "Priorität der Bestellung — beeinflusst die Lieferzeit" , example = "Normal")
            String prioritaet
    ) {}

    /**
     * Antwort nach erfolgreicher Bestellung.
     */
    public record BestellungResponseDto(
            @Schema(description = "Eindeutige Bestell-ID fuer Tracking", example = "BST-2026-00123")
            String bestellId,

            @Schema(description = "Aktueller Status der Bestellung")
            String status,

            @Schema(description = "Voraussichtliche Lieferzeit in Stunden", example = "4")
            Integer lieferzeitStunden,

            @Schema(description = "Menschenlesbare Nachricht fuer den Apotheker")
            String nachricht
    ) {}

    /**
     * Status-Abfrage fuer eine bestehende Bestellung.
     */
    public record BestellungStatusDto(
            String bestellId,
            String apothekenId,
            String medikament,
            Integer menge,
            String einheit,
            String status,
            LocalDateTime erstelltAm,
            LocalDateTime voraussichtlicheLieferung,
            String nachrichtFuerApotheker   // Klar formuliert, kein IT-Kauderwelsch!
    ) {}

    /**
     * Fehlerpayload — Fehlermeldung muss fuer Apotheker verstaendlich sein!
     * Kein "NullPointerException" oder Stack Trace nach aussen.
     */
    public record FehlerDto(
            @Schema(description = "Fehler-Code fuer IT-Support", example = "BESTELLUNG_NICHT_GEFUNDEN")
            String fehlerCode,

            @Schema(description = "Klare Fehlermeldung fuer den Apotheker — kein IT-Kauderwelsch!")
            String nachrichtFuerApotheker,

            @Schema(description = "Technische Details nur fuer IT (optional)")
            String technischeDetails
    ) {}

    /**
     * Liste aller Bestellungen einer Apotheke.
     */
    public record BestellungListeDto(
            String apothekenId,
            List<BestellungStatusDto> bestellungen,
            int anzahl
    ) {}
}
