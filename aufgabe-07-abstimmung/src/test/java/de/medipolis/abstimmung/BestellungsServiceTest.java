package de.medipolis.abstimmung;

import de.medipolis.abstimmung.model.Dtos.*;
import de.medipolis.abstimmung.service.BestellungsService;
import de.medipolis.abstimmung.exception.BestellungNichtGefundenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * AUFGABE 5: Unit-Tests schreiben
 * ============================================================
 *
 * Fuehre aus:
 *   mvn test -Dtest=BestellungsServiceTest
 */
class BestellungsServiceTest {

    private BestellungsService service;

    @BeforeEach
    void setUp() {
        service = new BestellungsService();
    }

    @Nested
    @DisplayName("erstelleBestellung()")
    class ErstelleBestellungTests {

        @Test
        @DisplayName("PASS: Gueltige Bestellung wird erstellt und BestellId generiert")
        void shouldCreateBestellungWithId() {
            // TODO: Test implementieren
            // Gegeben: gueltiger Request mit apothekenId="APO-JENA-001",
            //          medikament="Carboplatin", menge=5, einheit="mg", prioritaet="DRINGEND"
            // Erwarte:
            //   result.bestellId() ist nicht null und startet mit "BST-"
            //   result.status() == "EINGEGANGEN"
            //   result.lieferzeitStunden() == 2 (wegen DRINGEND)
            //   result.nachricht() ist nicht leer
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: Prioritaet NORMAL → 8 Stunden Lieferzeit")
        void shouldReturnNormalDeliveryTime() {
            // TODO: Test implementieren
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: Keine Prioritaet → 24 Stunden Lieferzeit")
        void shouldReturnDefaultDeliveryTime() {
            // TODO: Test implementieren
            fail("AUFGABE 5: Implementiere diesen Test!");
        }
    }

    @Nested
    @DisplayName("holeBestellungStatus()")
    class HoleBestellungStatusTests {

        @Test
        @DisplayName("PASS: Existierende Bestellung wird korrekt zurueckgegeben")
        void shouldReturnExistingBestellung() {
            // TODO: Test implementieren
            // Erst erstellen, dann abfragen
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("FAIL erwartet: Unbekannte BestellId → BestellungNichtGefundenException")
        void shouldThrowWhenBestellIdUnknown() {
            // TODO: Test implementieren
            // assertThatThrownBy(() -> service.holeBestellungStatus("UNBEKANNT-123"))
            //     .isInstanceOf(BestellungNichtGefundenException.class)
            fail("AUFGABE 5: Implementiere diesen Test!");
        }
    }

    @Nested
    @DisplayName("storniereBestellung()")
    class StorniereBestellungTests {

        @Test
        @DisplayName("PASS: EINGEGANGEN Bestellung kann storniert werden")
        void shouldCancelEingegangeneBestellung() {
            // TODO: Test implementieren
            // Erst erstellen, dann stornieren
            // Erwarte: result.status() == "STORNIERT"
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("FAIL erwartet: Nicht gefundene Bestellung → Exception")
        void shouldThrowWhenNotFound() {
            // TODO: Test implementieren
            fail("AUFGABE 5: Implementiere diesen Test!");
        }
    }

    @Nested
    @DisplayName("holeAlleBestellungen()")
    class HoleAlleBestellungenTests {

        @Test
        @DisplayName("PASS: Leere Liste wenn keine Bestellungen vorhanden")
        void shouldReturnEmptyListWhenNoOrders() {
            // TODO: Test implementieren
            // Keine Bestellung erstellt
            // Erwarte: result.anzahl() == 0, result.bestellungen() ist leer
            // WICHTIG: Kein Exception — leere Liste ist korrekt!
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: Nur Bestellungen der richtigen Apotheke werden zurueckgegeben")
        void shouldReturnOnlyCorrectApothekenBestellungen() {
            // TODO: Test implementieren
            // Zwei Bestellungen fuer APO-001 und eine fuer APO-002 erstellen
            // Erwarte: holeAlleBestellungen("APO-001").anzahl() == 2
            fail("AUFGABE 5: Implementiere diesen Test!");
        }
    }
}
