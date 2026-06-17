package de.medipolis.fhir.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Interne JPA Entity — wird in PostgreSQL gespeichert.
 * Wird durch den RezeptFhirMapper zu FHIR R4 MedicationRequest konvertiert.
 *
 * Deutsches Profil: ISiK (Informationstechnische Systeme im Krankenhaus)
 * PZN = Pharmazentralnummer — deutscher Standard fuer Medikamente
 */
@Entity
@Table(name = "rezepte_fhir")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rezept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "medikament", nullable = false)
    private String medikament;

    @Column(name = "dosierung_mg", nullable = false)
    private BigDecimal dosierungMg;

    // Einheit z.B. "mg", "ml", "IE"
    @Column(name = "einheit")
    @Builder.Default
    private String einheit = "mg";

    @Column(name = "arzt_name", nullable = false)
    private String arztName;

    // PZN = Pharmazentralnummer — kann null sein wenn unbekannt
    @Column(name = "pzn")
    private String pzn;

    @Column(name = "status")
    @Builder.Default
    private String status = "NEU";

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;
}
