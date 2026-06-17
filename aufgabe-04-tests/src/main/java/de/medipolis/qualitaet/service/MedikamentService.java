package de.medipolis.qualitaet.service;

import de.medipolis.qualitaet.client.KrankenkasseClient;
import de.medipolis.qualitaet.model.Dtos.*;
import de.medipolis.qualitaet.model.Medikament;
import de.medipolis.qualitaet.repository.MedikamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * MedikamentService — bereits vollstaendig implementiert.
 * Du musst ihn NICHT anfassen — nur testen!
 *
 * Was dieser Service tut:
 * 1. Medikament in DB speichern
 * 2. Krankenkasse um Genehmigung fragen
 * 3. MedikamentResponseDto zurueckgeben
 *
 * DEINE AUFGABE: Schreibe Tests dafuer (Unit + Integration)
 */
@Service
public class MedikamentService {

    private static final Logger log = LoggerFactory.getLogger(MedikamentService.class);

    private final MedikamentRepository repository;
    private final KrankenkasseClient krankenkasseClient;

    public MedikamentService(MedikamentRepository repository,
                             KrankenkasseClient krankenkasseClient) {
        this.repository = repository;
        this.krankenkasseClient = krankenkasseClient;
    }

    @Transactional
    public MedikamentResponseDto speichereUndPruefe(MedikamentRequestDto request) {
        log.info("Neues Medikament wird verarbeitet fuer patientId-Kategorie");

        // Validierung der Dosierung
        if (request.dosierungMg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Dosierung muss groesser als 0 sein");
        }

        // In DB speichern
        Medikament medikament = Medikament.builder()
                .name(request.name())
                .dosierungMg(request.dosierungMg())
                .einheit(request.einheit())
                .patientId(request.patientId())
                .status(Medikament.Status.NEU)
                .build();

        Medikament gespeichert = repository.save(medikament);
        log.info("Medikament gespeichert mit id={}", gespeichert.getId());

        String krankenkasseStatus;
        // Krankenkasse fragen
        try {
             krankenkasseStatus = krankenkasseClient.pruefeGenehmigung(
                    request.name(), request.patientId()
            );
        }catch (Exception e) {
            krankenkasseStatus = "UNBEKANNT";
        }

        return new MedikamentResponseDto(
                gespeichert.getId(),
                gespeichert.getName(),
                gespeichert.getDosierungMg(),
                gespeichert.getEinheit(),
                gespeichert.getPatientId(),
                gespeichert.getStatus().name(),
                krankenkasseStatus
        );
    }

    /**
     * Berechnet die gewichtsbasierte Dosierung.
     * Wird separat getestet — reine Business-Logik, kein DB-Aufruf.
     */
    public BigDecimal berechneGewichtsDosis(BigDecimal basisdosis, BigDecimal gewichtKg) {
        if (basisdosis == null || basisdosis.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Basisdosis muss groesser als 0 sein");
        }
        if (gewichtKg == null || gewichtKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Gewicht muss groesser als 0 sein");
        }

        // Formel: dosisProKg * gewicht, gerundet auf 2 Stellen
        BigDecimal dosisProKg = basisdosis.divide(gewichtKg, 4, java.math.RoundingMode.HALF_UP);
        return dosisProKg.multiply(gewichtKg).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
