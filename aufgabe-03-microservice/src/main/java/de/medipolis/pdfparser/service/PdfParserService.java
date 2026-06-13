package de.medipolis.pdfparser.service;

import de.medipolis.pdfparser.exception.PdfParseException;
import de.medipolis.pdfparser.model.Dtos.ParseErgebnisDto;
import de.medipolis.pdfparser.model.Dtos.PdfParseRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ============================================================
 * AUFGABE 1: PdfParserService implementieren
 * ============================================================
 *
 * KONTEXT:
 * Orchestra kann keine PDFs lesen. Es schickt den PDF-Inhalt
 * als Text an diesen Microservice. Du extrahierst die relevanten
 * Daten und gibst sie als sauberes JSON zurueck.
 *
 * In echten Projekten wuerde man Apache PDFBox oder ein KI-Tool
 * nutzen. Hier simulieren wir das PDF als strukturierten Text:
 *
 * Beispiel PDF-Inhalt (als String):
 * "PATIENT_ID:PAT-001;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg"
 *
 * Format: KEY:WERT;KEY:WERT;...
 *
 * ------------------------------------------------------------
 * METHODE 1: parsePdf()
 * ------------------------------------------------------------
 * Parameter:
 *   - request      : PdfParseRequestDto
 *   - correlationId: String
 *
 * Rueckgabe: ParseErgebnisDto
 *
 * Schritt 1 — Logging:
 *   log.info() mit correlationId und quellSystem
 *   NIEMALS Patientendaten ins Log! (DSGVO)
 *
 * Schritt 2 — Felder extrahieren:
 *   Rufe extrahiereFelder() auf mit request.pdfInhalt()
 *   Ergebnis: ein String-Array oder Map mit den Werten
 *
 * Schritt 3 — Validierung:
 *   Rufe validiereFelder() auf
 *   Bei Fehler: PdfParseException wird geworfen
 *
 * Schritt 4 — ParseErgebnisDto bauen:
 *   - correlationId  → unveraendert
 *   - patientId      → extrahierter Wert fuer "PATIENT_ID"
 *   - medikament     → extrahierter Wert fuer "MEDIKAMENT"
 *   - dosierungMg    → extrahierter Wert fuer "DOSIERUNG" als BigDecimal
 *   - einheit        → extrahierter Wert fuer "EINHEIT"
 *   - status         → "ERFOLGREICH"
 *
 * Schritt 5 — Ergebnis zurueckgeben
 *
 * ------------------------------------------------------------
 * METHODE 2: extrahiereFelder() — package-private (fuer Tests!)
 * ------------------------------------------------------------
 * Parameter: pdfInhalt (String)
 * Rueckgabe: java.util.Map<String, String>
 *
 * Das PDF-Format ist: "KEY1:WERT1;KEY2:WERT2;KEY3:WERT3"
 *
 * Beispiel:
 *   Eingabe:  "PATIENT_ID:PAT-001;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg"
 *   Ausgabe:  { "PATIENT_ID" -> "PAT-001",
 *               "MEDIKAMENT" -> "Carboplatin",
 *               "DOSIERUNG"  -> "450.5",
 *               "EINHEIT"    -> "mg" }
 *
 * Implementierungs-Tipp:
 *   1. Split den String nach ";" → bekommst ein Array von "KEY:WERT" Paaren
 *   2. Jedes Paar wieder nach ":" splitten
 *   3. In eine Map<String, String> einfuegen
 *
 * Wenn pdfInhalt null oder leer ist:
 *   → wirf IllegalArgumentException("PDF-Inhalt ist leer")
 *
 * ------------------------------------------------------------
 * METHODE 3: validiereFelder() — private
 * ------------------------------------------------------------
 * Parameter:
 *   - felder       : Map<String, String>
 *   - correlationId: String
 *
 * Pruefe:
 *   1. "PATIENT_ID" muss vorhanden und nicht leer sein
 *      → PdfParseException("PATIENT_ID fehlt im PDF", correlationId)
 *
 *   2. "MEDIKAMENT" muss vorhanden und nicht leer sein
 *      → PdfParseException("MEDIKAMENT fehlt im PDF", correlationId)
 *
 *   3. "DOSIERUNG" muss vorhanden und eine gueltige Zahl > 0 sein
 *      → PdfParseException("DOSIERUNG ungueltig im PDF", correlationId)
 *      Tipp: Nutze try-catch beim Parsen mit new BigDecimal(wert)
 *
 *   4. "EINHEIT" muss vorhanden und nicht leer sein
 *      → PdfParseException("EINHEIT fehlt im PDF", correlationId)
 *
 * ------------------------------------------------------------
 * Interview-Fragen:
 *
 * "Warum ist extrahiereFelder() package-private und nicht private?"
 *  → Damit Unit-Tests sie direkt testen koennen ohne den ganzen
 *    Service aufrufen zu muessen. Bessere Testbarkeit.
 *
 * "Was wuerdest du in einem echten Projekt statt dem Text-Format nutzen?"
 *  → Apache PDFBox fuer PDF-Parsing, oder einen KI-Service
 *    der den PDF-Inhalt analysiert. Aber das Muster bleibt gleich:
 *    extrahieren → validieren → transformieren → zurueckgeben.
 *
 * "Warum BigDecimal fuer die Dosierung?"
 *  → double hat Floating-Point Ungenauigkeiten (0.1 + 0.2 = 0.300...04).
 *    Bei Medikamenten-Dosierungen im Gesundheitswesen ist das
 *    lebensgefaehrlich. BigDecimal ist exakt.
 *    @Service
 * public class PdfParserService {
 *
 *     private static final Logger log = LoggerFactory.getLogger(PdfParserService.class);
 *
 *     public ParseErgebnisDto parsePdf(PdfParseRequestDto request, String correlationId) {
 *         // TODO: Implementiere Schritt 1-5
 *         throw new UnsupportedOperationException("AUFGABE 1: Implementiere parsePdf()!");
 *     }
 *
 *     // package-private damit Tests sie direkt testen koennen
 *     Map<String, String> extrahiereFelder(String pdfInhalt) {
 *         // TODO: String parsen und in Map umwandeln
 *         // Format: "KEY1:WERT1;KEY2:WERT2"
 *         throw new UnsupportedOperationException("AUFGABE 1: Implementiere extrahiereFelder()!");
 *     }
 *
 *     private void validiereFelder(Map<String, String> felder, String correlationId) {
 *         // TODO: Alle 4 Felder pruefen
 *         // PdfParseException bei fehlenden/ungueltigen Feldern
 *         throw new UnsupportedOperationException("AUFGABE 1: Implementiere validiereFelder()!");
 *     }
 * }
 */

public class PdfParserService { 

    private static final Logger log = LoggerFactory.getLogger(PdfParserService.class);

    public ParseErgebnisDto parsePdf(PdfParseRequestDto request, String correlationId) {

        validiereFelder(extrahiereFelder(request.pdfInhalt()), correlationId);

        Map<String, String> extrahierte = extrahiereFelder(request.pdfInhalt());



        ParseErgebnisDto result = new ParseErgebnisDto(
                correlationId,
                extrahierte.get("PATIENT_ID"),
                extrahierte.get("MEDIKAMENT"),
                new BigDecimal(extrahierte.get("DOSIERUNG")),
                extrahierte.get("EINHEIT"),
                "ERFOLGREICH"
                );



        return result;

     }
     // package-private damit Tests sie direkt testen koennen
     Map<String, String> extrahiereFelder(String pdfInhalt) {

         if (pdfInhalt == null || pdfInhalt.trim().isEmpty()) {
             throw new IllegalArgumentException("PDF-Inhalt ist leer");
         }

        Map<String, String> fields = new HashMap<>();

        List<String>  pdfList = Arrays.asList(pdfInhalt.trim().split(";"));
        for (String field : pdfList) {
            String[] keyValue = field.split(":");
            fields.put(keyValue[0].trim(), keyValue[1].trim());
        }

        return fields;


    }
    private void validiereFelder(Map<String, String> felder, String correlationId) {
        if (felder.get("PATIENT_ID") == null || felder.get("PATIENT_ID").trim().isEmpty()) {
            throw new PdfParseException("PATIENT_ID fehlt im PDF", correlationId);
        }
        if (felder.get("MEDIKAMENT") == null || felder.get("MEDIKAMENT").trim().isEmpty()) {
            throw new PdfParseException("MEDIKAMENT fehlt im PDF", correlationId);
        }
        if (felder.get("DOSIERUNG") == null || felder.get("DOSIERUNG").trim().isEmpty()) {
            throw new PdfParseException("DOSIERUNG fehlt im PDF", correlationId);
        }
        if (felder.get("EINHEIT") == null || felder.get("EINHEIT").trim().isEmpty()) {
            throw new PdfParseException("EINHEIT fehlt im PDF", correlationId);
        }

        try{
            BigDecimal dosierung = new BigDecimal(felder.get("DOSIERUNG"));
            if (dosierung.compareTo(BigDecimal.ZERO) == 0) {
                throw new PdfParseException("DOSIERUNG ungueltig im PDF", correlationId);
            }
        }catch (NumberFormatException e){
            throw new PdfParseException("DOSIERUNG ungueltig im PDF", correlationId);
        }


    }
  }
