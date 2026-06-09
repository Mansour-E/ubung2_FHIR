package de.medipolis.qualitaet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class Dtos {

    public record MedikamentRequestDto(
            @NotBlank(message = "Name darf nicht leer sein")
            String name,

            @NotNull(message = "Dosierung ist Pflichtfeld")
            @Positive(message = "Dosierung muss positiv sein")
            BigDecimal dosierungMg,

            @NotBlank(message = "Einheit darf nicht leer sein")
            String einheit,

            @NotBlank(message = "Patient-ID darf nicht leer sein")
            String patientId
    ) {}

    public record MedikamentResponseDto(
            Long id,
            String name,
            BigDecimal dosierungMg,
            String einheit,
            String patientId,
            String status,
            String krankenkasseStatus  // Antwort vom externen System
    ) {}

    public record FehlerDto(
            String fehlerCode,
            String nachricht
    ) {}
}
