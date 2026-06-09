package de.medipolis.ki.service;

import de.medipolis.ki.model.Dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ============================================================
 * AUFGABE 1: AnonymisierungsService implementieren
 * ============================================================
 *
 * KONTEXT:
 * Bevor ein Arztbrief zur KI geschickt wird, MUSS er
 * anonymisiert werden. Das ist die wichtigste DSGVO-Regel
 * in diesem Projekt.
 *
 * Die Anonymisierung ersetzt echte Daten durch Platzhalter.
 *
 * ------------------------------------------------------------
 * METHODE 1: anonymisiere()
 * ------------------------------------------------------------
 * Parameter: arztbriefText (String)
 * Rueckgabe: AnonymisierterArztbriefDto
 *
 * Schritt 1 — AnonymisierungsId generieren:
 *   String anonymisierungsId = UUID.randomUUID().toString();
 *
 * Schritt 2 — Namen ersetzen:
 *   Rufe ersetzeNamen() auf
 *   Tipp: Einfache Heuristik — Woerter die mit Grossbuchstaben
 *   beginnen und laenger als 3 Zeichen sind ersetzen
 *
 * Schritt 3 — Daten ersetzen:
 *   Rufe ersetzeDaten() auf
 *
 * Schritt 4 — IDs ersetzen:
 *   Rufe ersetzeIds() auf
 *
 * Schritt 5 — AnonymisierterArztbriefDto zurueckgeben
 *
 * LOGGING:
 *   log.info("Arztbrief anonymisiert, anonymisierungsId={}", id)
 *   NIEMALS den originalen Text loggen!
 *
 * ------------------------------------------------------------
 * METHODE 2: ersetzeNamen() — package-private (fuer Tests)
 * ------------------------------------------------------------
 * Parameter: text (String)
 * Rueckgabe: String
 *
 * Ersetze folgende Muster:
 *   "Patient [Name]"  → "Patient [ANONYM]"
 *   "Dr. [Name]"      → "Dr. [ANONYM]"
 *   "Arzt: [Name]"    → "Arzt: [ANONYM]"
 *
 * Tipp: Nutze String.replaceAll() mit Regex:
 *   text.replaceAll("Patient\\s+\\w+", "Patient [ANONYM]")
 *   text.replaceAll("Dr\\.\\s+\\w+", "Dr. [ANONYM]")
 *
 * ------------------------------------------------------------
 * METHODE 3: ersetzeDaten() — package-private (fuer Tests)
 * ------------------------------------------------------------
 * Parameter: text (String)
 * Rueckgabe: String
 *
 * Ersetze Datumsformate:
 *   DD.MM.YYYY → [DATUM]
 *   Regex: "\\d{2}\\.\\d{2}\\.\\d{4}"
 *
 * Ersetze Geburtsdaten:
 *   "geb. DD.MM.YYYY" → "geb. [DATUM]"
 *
 * ------------------------------------------------------------
 * METHODE 4: ersetzeIds() — package-private (fuer Tests)
 * ------------------------------------------------------------
 * Parameter: text (String)
 * Rueckgabe: String
 *
 * Ersetze Patienten-IDs:
 *   "PAT-XXXXX" → "PAT-[ID]"
 *   Regex: "PAT-\\d+"
 *
 * Ersetze Versicherungsnummern (10 Buchstaben/Zahlen):
 *   Regex: "[A-Z]{1}\\d{9}"  → "[VERSICHERUNGSNR]"
 *
 * ------------------------------------------------------------
 * Interview-Fragen:
 *
 * "Warum anonymisierst du BEVOR der KI-Call?"
 *  → DSGVO Zero Data Leakage Policy.
 *    Echte Patientendaten in oeffentliche KI = sofortiger
 *    Compliance-Verstoss gemaess Art. 9 DSGVO (Gesundheitsdaten
 *    = besondere Kategorie). Strafbar!
 *
 * "Ist diese Regex-Anonymisierung perfekt?"
 *  → Nein — und das sage ich auch im Interview offen.
 *    In Produktion wuerde man ein spezialisiertes
 *    NLP-Anonymisierungstool nutzen (z.B. presidio von Microsoft).
 *    Aber das Prinzip — erst anonymisieren, dann zur KI — ist richtig.
 */
@Service
public class AnonymisierungsService {

    private static final Logger log = LoggerFactory.getLogger(AnonymisierungsService.class);

    public AnonymisierterArztbriefDto anonymisiere(String arztbriefText) {
        // TODO: Implementiere Schritt 1-5
        throw new UnsupportedOperationException("AUFGABE 1: Implementiere anonymisiere()!");
    }

    // package-private fuer direkte Unit-Tests
    String ersetzeNamen(String text) {
        // TODO: Patient [Name], Dr. [Name], Arzt: [Name] ersetzen
        throw new UnsupportedOperationException("AUFGABE 1: Implementiere ersetzeNamen()!");
    }

    // package-private fuer direkte Unit-Tests
    String ersetzeDaten(String text) {
        // TODO: Datumsformate DD.MM.YYYY ersetzen
        throw new UnsupportedOperationException("AUFGABE 1: Implementiere ersetzeDaten()!");
    }

    // package-private fuer direkte Unit-Tests
    String ersetzeIds(String text) {
        // TODO: PAT-XXXXX und Versicherungsnummern ersetzen
        throw new UnsupportedOperationException("AUFGABE 1: Implementiere ersetzeIds()!");
    }
}
