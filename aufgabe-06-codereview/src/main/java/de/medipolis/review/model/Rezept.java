package de.medipolis.review.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rezepte")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rezept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "medikament")
    private String medikament;

    @Column(name = "dosierung_mg")
    private BigDecimal dosierungMg;

    @Column(name = "arzt_name")
    private String arztName;

    @Column(name = "status")
    private String status;

    @Column(name = "erstellt_am")
    private LocalDateTime erstelltAm;
}
