package de.medipolis.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RezeptRequestDto(
        @NotBlank(message = "PatientId darf nicht leer sein")
        String patientId,

        @NotBlank(message = "PatientName darf nicht leer sein")
        String patientName,

        @NotBlank(message = "Medikament darf nicht leer sein")
        String medikament,

        @NotNull(message = "Dosierung darf nicht null sein")
        @Positive(message = "dosierung soll großer als 0 sein")
        BigDecimal dosierung,

        @NotBlank(message = "arztName darf nicht leer sein")
        String arztName
) {}
