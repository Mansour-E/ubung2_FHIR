package de.medipolis.review.service;

import de.medipolis.review.model.Rezept;
import de.medipolis.review.repository.RezeptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ============================================================
 * PULL REQUEST von Kollege "Jonas" — Deine Aufgabe: CODE REVIEW
 * ============================================================
 *
 * Jonas hat diesen Service fuer die Rezept-Verwaltung gebaut.
 * Er hat einen Pull Request geoeffnet und du bist der Reviewer.
 *
 * Der Code kompiliert und laeuft — aber er hat ernste Probleme.
 *
 * DEINE AUFGABE:
 * 1. Lies den Code sorgfaeltig durch (ohne zu runen!)
 * 2. Finde alle 8 Probleme
 * 3. Erklaere jedes Problem in der REVIEW_LISTE.md
 * 4. Fixe jeden Fehler direkt im Code
 *
 * Kategorie-Labels (wie im echten Code-Review):
 * [blocking]   → Muss gefixt werden bevor Merge erlaubt ist
 * [suggestion] → Verbesserungsvorschlag, nicht zwingend
 * [nitpick]    → Kleinigkeit, optional
 */
@Service
public class RezeptService {

    private static final Logger log = LoggerFactory.getLogger(RezeptService.class);

    private final RezeptRepository repository;

    // ❌ REVIEW-PROBLEM #1 [blocking] — SECURITY
    // Was ist hier falsch? Wo gehoert das hin?
    private static final String DB_PASSWORD = "SuperGeheim123!";

    public RezeptService(RezeptRepository repository) {
        this.repository = repository;
    }

    public Rezept speichereRezept(String patientId, String patientName,
                                   String medikament, BigDecimal dosierungMg,
                                   String arztName) {

        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        // ❌ REVIEW-PROBLEM #2 [blocking] — DSGVO
        // Was ist mit dieser Log-Zeile falsch?
        log.info("Neues Rezept: Patient={}, Name={}, Medikament={}, Arzt={}",
                patientId, patientName, medikament, arztName);

        // ❌ REVIEW-PROBLEM #3 [blocking] — MDC nicht gecleart
        // Was fehlt hier? Was passiert ohne den Fix?

        // Validierung
        if (dosierungMg == null || dosierungMg.doubleValue() <= 0) {
            throw new IllegalArgumentException("Dosierung ungueltig");
        }

        Rezept rezept = Rezept.builder()
                .patientId(patientId)
                .patientName(patientName)
                .medikament(medikament)
                .dosierungMg(dosierungMg)
                .arztName(arztName)
                .status("NEU")
                .erstelltAm(LocalDateTime.now())
                .build();

        return repository.save(rezept);
    }

    // ❌ REVIEW-PROBLEM #4 [blocking] — Falsche Vergleichslogik
    // Diese Methode soll alle Rezepte mit Dosierung >= 500mg als DRINGEND markieren.
    // Was ist hier falsch? (Tipp: du kennst diesen Bug schon aus Projekt 1!)
    public String bestimmeDringlichkeit(BigDecimal dosierungMg) {
        BigDecimal schwellenwert = new BigDecimal("500");
        if (dosierungMg.compareTo(schwellenwert) == 1) {
            return "DRINGEND";
        }
        return "NORMAL";
    }

    // ❌ REVIEW-PROBLEM #5 [blocking] — N+1 Query Problem
    // Diese Methode macht bei 100 Aerzten = 101 DB-Abfragen!
    // Finde das Problem und erklaere den Fix.
    public List<String> alleRezepteNachAerzten(List<String> arztNamen) {
        List<String> ergebnis = new ArrayList<>();
        for (String arzt : arztNamen) {
            // Pro Arzt eine separate DB-Abfrage — das ist das N+1 Problem!
            List<Rezept> rezepte = repository.findByArzt(arzt);
            for (Rezept r : rezepte) {
                ergebnis.add(arzt + ": " + r.getMedikament());
            }
        }
        return ergebnis;
    }

    // ❌ REVIEW-PROBLEM #6 [blocking] — Exception wird verschluckt
    // Was ist hier falsch? Was passiert wenn die DB nicht erreichbar ist?
    public Rezept holeRezept(Long id) {
        try {
            return repository.findById(id).orElse(null);
        } catch (Exception e) {
            // Fehler wird komplett ignoriert!
            return null;
        }
    }

    // ❌ REVIEW-PROBLEM #7 [suggestion] — Logik gehoert nicht in den Service
    // Diese Methode formatiert einen String fuer die Anzeige.
    // Wo sollte Darstellungslogik in einer sauberen Architektur sitzen?
    public String formatiereRezeptFuerAnzeige(Rezept rezept) {
        return "Patient: " + rezept.getPatientName() +
               " | Medikament: " + rezept.getMedikament() +
               " | Dosierung: " + rezept.getDosierungMg() + "mg" +
               " | Status: " + rezept.getStatus();
    }

    // ❌ REVIEW-PROBLEM #8 [nitpick] — Schlechte Methoden-Benennung + Magic Number
    // Was sind hier zwei kleine aber wichtige Probleme?
    public boolean check(BigDecimal d) {
        return d.compareTo(new BigDecimal("1000")) > 0;
    }
}
