package de.medipolis.ki.model;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTOs fuer die KI-Arztbrief-Analyse.
 */
public class Dtos {

    /**
     * Eingehender Arztbrief von Orchestra.
     * WICHTIG: Muss vor dem KI-Call anonymisiert werden!
     */
    public record ArztbriefRequestDto(
            @NotBlank(message = "Arztbrief-Text darf nicht leer sein")
            String arztbriefText,

            @NotBlank(message = "Quell-System muss angegeben werden")
            String quellSystem
    ) {}

    /**
     * Anonymisierte Version — diese Version geht zur KI.
     * Keine echten Patientendaten!
     */
    public record AnonymisierterArztbriefDto(
            String anonymisierterText,
            String anonymisierungsId   // Internes Mapping fuer Rueckverfolgung
    ) {}

    /**
     * KI-Request-Body der an die externe KI-API geschickt wird.
     * Simuliert OpenAI-Format.
     */
    public record KiApiRequestDto(
            String model,
            List<KiNachrichtDto> messages
    ) {}

    public record KiNachrichtDto(
            String role,    // "system" oder "user"
            String content
    ) {}

    /**
     * Antwort der KI-API.
     */
    public record KiApiResponseDto(
            List<KiAuswahlDto> choices
    ) {}

    public record KiAuswahlDto(
            KiNachrichtDto message
    ) {}

    /**
     * Extrahierte Medikamenten-Daten aus dem Arztbrief.
     * Das Ergebnis geht zurueck an Orchestra.
     */
    public record ExtrahierteMedikamentendatenDto(
            String correlationId,
            String medikament,
            BigDecimal dosierungMg,
            String einheit,
            String diagnoseCode,    // ICD-10 Code — kein Patientenname!
            String status,          // "ERFOLGREICH" | "KI_FEHLER" | "NICHT_GEFUNDEN"
            String hinweis          // Optionaler Hinweis wenn KI unsicher war
    ) {}

    /**
     * Fehlerpayload.
     */
    public record FehlerDto(
            String correlationId,
            String fehlerCode,
            String nachricht
    ) {}
}
