package de.medipolis.qualitaet.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "medikamente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medikament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "dosierung_mg", nullable = false)
    private BigDecimal dosierungMg;

    @Column(nullable = false)
    private String einheit;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "erstellt_am", nullable = false)
    private LocalDateTime erstelltAm;

    @PrePersist
    protected void onCreate() {
        erstelltAm = LocalDateTime.now();
        if (status == null) status = Status.NEU;
    }

    public enum Status {
        NEU, GENEHMIGT, ABGELEHNT
    }
}
