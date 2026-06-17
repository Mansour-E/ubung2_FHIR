package de.medipolis.review.service;

import de.medipolis.review.model.Rezept;
import de.medipolis.review.repository.RezeptRepository;
import jakarta.persistence.EntityNotFoundException;
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

    private static final BigDecimal MAX_DOSIERUNG = BigDecimal.valueOf(1000);

    // ❌ REVIEW-PROBLEM #1 [blocking] — SECURITY
    // Was ist hier falsch? Wo gehoert das hin?
    // password soll nicht in code sein sondern in Enviroment variable

    public RezeptService(RezeptRepository repository) {
        this.repository = repository;
    }

    public Rezept speichereRezept(String patientId, String patientName,
                                   String medikament, BigDecimal dosierungMg,
                                   String arztName) {

        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        try{
            // ❌ REVIEW-PROBLEM #2 [blocking] — DSGVO
            // Was ist mit dieser Log-Zeile falsch?
            // patienten information darf nicht geloggt werden
            log.info("Neues Rezept: correlationId={}", correlationId);

            // ❌ REVIEW-PROBLEM #3 [blocking] — MDC nicht gecleart
            // Was fehlt hier? Was passiert ohne den Fix?
            // MDC clean ist nicht aufgerufen

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

        }finally{
            MDC.clear();
        }




    }

    // ❌ REVIEW-PROBLEM #4 [blocking] — Falsche Vergleichslogik
    // Diese Methode soll alle Rezepte mit Dosierung >= 500mg als DRINGEND markieren.
    // Was ist hier falsch? (Tipp: du kennst diesen Bug schon aus Projekt 1!)
    // wegen compareTo sollte grosser als 0 raus kommen
    public String bestimmeDringlichkeit(BigDecimal dosierungMg) {
        BigDecimal schwellenwert = new BigDecimal("500");
        if (dosierungMg.compareTo(schwellenwert) >= 0) {
            return "DRINGEND";
        }
        return "NORMAL";
    }

    // ❌ REVIEW-PROBLEM #5 [blocking] — N+1 Query Problem
    // Diese Methode macht bei 100 Aerzten = 101 DB-Abfragen!
    // Finde das Problem und erklaere den Fix.
    public List<String> alleRezepteNachAerzten(List<String> arztNamen) {
        List<String> ergebnis = new ArrayList<>();

            // Pro Arzt eine separate DB-Abfrage — das ist das N+1 Problem!
            List<Rezept> alleRezepte = repository.findByArztIn(arztNamen);
            for (Rezept r : alleRezepte) {
                ergebnis.add(r.getArztName() + ": " + r.getMedikament());
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
             throw new EntityNotFoundException(e.getMessage());
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
    // Atwort : method ist falsch  das schmeisst true raus wenn d großer als 1000 ist nicht gleich oder kleiner
    public boolean istUeberdosis(BigDecimal dosierungMg) {
        return dosierungMg.compareTo(MAX_DOSIERUNG) > 0;
    }
}
