package de.medipolis.abstimmung.service;

import de.medipolis.abstimmung.exception.BestellungNichtGefundenException;
import de.medipolis.abstimmung.model.Dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ============================================================
 * AUFGABE 1: BestellungsService implementieren
 * ============================================================
 *
 * KONTEXT:
 * Der Apotheker Klaus Meier aus Jena hat folgende Anforderung gestellt:
 *
 * "Ich brauche eine Moeglichkeit:
 *  1. Neue Medikamenten-Bestellungen aufzugeben
 *  2. Den Status meiner Bestellungen abzufragen
 *  3. Alle meine offenen Bestellungen auf einmal zu sehen
 *  4. Eine Bestellung zu stornieren wenn ich sie falsch eingegeben habe"
 *
 * Du hast das in einen technischen API-Contract uebersetzt (siehe Controller).
 * Jetzt implementierst du die Business-Logik dahinter.
 *
 * HINWEIS: Wir nutzen eine In-Memory Map statt einer DB
 * (kein Flyway/PostgreSQL noetig fuer diese Aufgabe).
 *
 * ------------------------------------------------------------
 * METHODE 1: erstelleBestellung()
 * ------------------------------------------------------------
 * Parameter: BestellungRequestDto, apothekenId (String)
 * Rueckgabe: BestellungResponseDto
 *
 * Schritt 1: BestellId generieren
 *   Format: "BST-" + Jahr + "-" + zufaellige 5-stellige Zahl
 *   Beispiel: "BST-2026-00123"
 *   Tipp: String.format("BST-%d-%05d", LocalDateTime.now().getYear(),
 *                        new Random().nextInt(99999))
 *
 * Schritt 2: Lieferzeit bestimmen basierend auf Prioritaet:
 *   "DRINGEND" → 2 Stunden
 *   "NORMAL"   → 8 Stunden
 *   null/andere → 24 Stunden
 *
 * Schritt 3: BestellungStatusDto in der Map speichern:
 *   Felder:
 *   - bestellId      → aus Schritt 1
 *   - apothekenId    → aus Parameter
 *   - medikament     → aus request
 *   - menge          → aus request
 *   - einheit        → aus request
 *   - status         → "EINGEGANGEN"
 *   - erstelltAm     → LocalDateTime.now()
 *   - voraussichtlicheLieferung → now() + lieferzeit Stunden
 *   - nachrichtFuerApotheker → klar formuliert, KEIN IT-Kauderwelsch!
 *     Beispiel: "Ihre Bestellung wurde erfolgreich aufgenommen.
 *                Voraussichtliche Lieferung in X Stunden."
 *
 * Schritt 4: BestellungResponseDto zurueckgeben
 *
 * WICHTIG — DSGVO Logging:
 *   log.info() mit bestellId und apothekenId
 *   NIEMALS Medikament oder Menge ins Log!
 *
 * ------------------------------------------------------------
 * METHODE 2: holeBestellungStatus()
 * ------------------------------------------------------------
 * Parameter: bestellId (String)
 * Rueckgabe: BestellungStatusDto
 *
 * - In der Map nachschauen
 * - Wenn nicht gefunden: BestellungNichtGefundenException werfen
 * - Nachricht fuer den Apotheker muss klar und freundlich sein!
 *   NICHT: "Entity not found in repository"
 *   SONDERN: "Wir konnten keine Bestellung mit dieser ID finden.
 *              Bitte pruefen Sie die Bestell-ID."
 *
 * ------------------------------------------------------------
 * METHODE 3: holeAlleBestellungen()
 * ------------------------------------------------------------
 * Parameter: apothekenId (String)
 * Rueckgabe: BestellungListeDto
 *
 * - Alle Bestellungen fuer diese apothekenId aus der Map filtern
 * - BestellungListeDto mit Liste und Anzahl zurueckgeben
 * - Wenn keine gefunden: leere Liste (keine Exception!)
 *
 * ------------------------------------------------------------
 * METHODE 4: storniereBestellung()
 * ------------------------------------------------------------
 * Parameter: bestellId (String)
 * Rueckgabe: BestellungResponseDto
 *
 * - Bestellung aus Map holen
 * - Wenn nicht gefunden: BestellungNichtGefundenException
 * - Status auf "STORNIERT" setzen
 * - Nur wenn Status "EINGEGANGEN" ist — sonst Exception:
 *   "Diese Bestellung kann nicht mehr storniert werden,
 *    da sie bereits in Bearbeitung ist."
 * - Zurueck in Map speichern
 * - BestellungResponseDto mit Status "STORNIERT" zurueckgeben
 */
@Service
public class BestellungsService {

    private static final Logger log = LoggerFactory.getLogger(BestellungsService.class);

    // In-Memory Speicher — kein DB noetig fuer diese Aufgabe
    private final Map<String, BestellungStatusDto> bestellungen = new ConcurrentHashMap<>();

    public BestellungResponseDto erstelleBestellung(BestellungRequestDto request, String apothekenId){

        String bestellungId = String.format("BST-%d-%05d",
                LocalDateTime.now().getYear(), new Random().nextInt(99999));

        int lieferZeit = berechneLiferzeit(request.prioritaet());

        BestellungStatusDto status = new BestellungStatusDto(
                bestellungId,
                apothekenId,
                request.medikament(),
                request.menge(),
                request.einheit(),
                "EINGEGANGEN",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(lieferZeit),
                "Ihre Bestellung wurde erfolgreich aufgenommen. Voraussichtliche Lieferung in " + lieferZeit + " Stunden."
        );

        bestellungen.put(bestellungId, status);

        log.info("Neue Bestellung erstellt: bestellId={}, apothekenId={}", bestellungId, apothekenId);

        return new BestellungResponseDto(
                bestellungId,
                "EINGEGANGEN",
                lieferZeit,
                "Ihre Bestellung wurde erfolgreich aufgenommen."
        );


    }

    public BestellungStatusDto holeBestellungStatus(String bestellungId){

        BestellungStatusDto status = bestellungen.get(bestellungId);

        if (status == null) {
            throw new BestellungNichtGefundenException( "Wir konnten keine Bestellung mit dieser ID finden. Bitte pruefen Sie die Bestell-ID.");
        }

        return status;
    }

    public BestellungListeDto holeAlleBestellungen(String apothekenId){


            List<BestellungStatusDto> status = bestellungen.values().stream().filter(b -> b.apothekenId().equals(apothekenId)).toList();
            return new BestellungListeDto(apothekenId , status , status.size());


    }

    public BestellungResponseDto storniereBestellung(String bestellungId){

        BestellungStatusDto bestellung =  bestellungen.get(bestellungId);

        if (bestellung == null) {
            throw new BestellungNichtGefundenException("Wir konnten keine Bestellung mit dieser ID finden. Bitte pruefen Sie die Bestell-ID.");
        }

        if (!bestellung.status().equals("EINGEGANGEN")) {
            throw new IllegalStateException("Diese Bestellung kann nicht mehr storniert werden, da sie bereits in Bearbeitung ist.");
        }
        BestellungStatusDto storniert = new BestellungStatusDto(
                bestellung.bestellId(), bestellung.apothekenId(), bestellung.medikament(),
                bestellung.menge(), bestellung.einheit(), "STORNIERT",
                bestellung.erstelltAm(), bestellung.voraussichtlicheLieferung(),
                "Ihre Bestellung wurde erfolgreich storniert."
        );
        bestellungen.put(bestellungId, storniert);
        return new BestellungResponseDto(bestellungId, "STORNIERT", 0, "Ihre Bestellung wurde storniert.");
    }

    // Hilfsmethode — bereits implementiert
    private int berechneLiferzeit(String prioritaet) {
        if (prioritaet == null) return 24;
        return switch (prioritaet.toUpperCase()) {
            case "DRINGEND" -> 2;
            case "NORMAL"   -> 8;
            default         -> 24;
        };
    }
}
