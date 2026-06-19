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


            BestellungRequestDto request = new BestellungRequestDto("APO-JENA-001", "Carboplatin", 5, "mg", "DRINGEND");
            BestellungResponseDto status = service.erstelleBestellung(request, "APO-JENA-001");

            assertThat(status).isNotNull();
            assertThat(status.bestellId()).isNotNull().startsWith("BST-");
            assertThat(status.status()).isEqualTo("EINGEGANGEN");
            assertThat(status.lieferzeitStunden()).isEqualTo(2);
            assertThat(status.nachricht()).isNotEmpty();

            // TODO: Test implementieren
            // Gegeben: gueltiger Request mit apothekenId="APO-JENA-001",
            //          medikament="Carboplatin", menge=5, einheit="mg", prioritaet="DRINGEND"
            // Erwarte:
            //   result.bestellId() ist nicht null und startet mit "BST-"
            //   result.status() == "EINGEGANGEN"
            //   result.lieferzeitStunden() == 2 (wegen DRINGEND)
            //   result.nachricht() ist nicht leer
        }

        @Test
        @DisplayName("PASS: Prioritaet NORMAL → 8 Stunden Lieferzeit")
        void shouldReturnNormalDeliveryTime() {
            BestellungRequestDto request = new BestellungRequestDto("APO-JENA-001", "Carboplatin", 5, "mg", "NORMAL");
            BestellungResponseDto status = service.erstelleBestellung(request, "APO-JENA-001");

            assertThat(status.lieferzeitStunden()).isEqualTo(8);
        }

        @Test
        @DisplayName("PASS: Keine Prioritaet → 24 Stunden Lieferzeit")
        void shouldReturnDefaultDeliveryTime() {
            BestellungRequestDto request = new BestellungRequestDto("APO-JENA-001", "Carboplatin", 5, "mg", null);
            BestellungResponseDto status = service.erstelleBestellung(request, "APO-JENA-001");

            assertThat(status.lieferzeitStunden()).isEqualTo(24);
        }
    }

    @Nested
    @DisplayName("holeBestellungStatus()")
    class HoleBestellungStatusTests {

        @Test
        @DisplayName("PASS: Existierende Bestellung wird korrekt zurueckgegeben")
        void shouldReturnExistingBestellung() {

            BestellungRequestDto request = new BestellungRequestDto("APO-JENA-001", "Carboplatin", 5, "mg", null);
            BestellungResponseDto bestellung = service.erstelleBestellung(request, request.apothekenId());
            BestellungStatusDto status = service.holeBestellungStatus(bestellung.bestellId());

            assertThat(status).isNotNull();
            assertThat(bestellung.bestellId()).isEqualTo(status.bestellId());
        }

        @Test
        @DisplayName("FAIL erwartet: Unbekannte BestellId → BestellungNichtGefundenException")
        void shouldThrowWhenBestellIdUnknown() {

            assertThatThrownBy(() -> service.holeBestellungStatus("UBEKANNT-123")).isInstanceOf(BestellungNichtGefundenException.class);
            // TODO: Test implementieren
            // assertThatThrownBy(() -> service.holeBestellungStatus("UNBEKANNT-123"))
            //     .isInstanceOf(BestellungNichtGefundenException.class)
        }
    }

    @Nested
    @DisplayName("storniereBestellung()")
    class StorniereBestellungTests {

        @Test
        @DisplayName("PASS: EINGEGANGEN Bestellung kann storniert werden")
        void shouldCancelEingegangeneBestellung() {
            BestellungRequestDto request = new BestellungRequestDto("APO-JENA-001", "Carboplatin", 5, "mg", null);
            BestellungResponseDto bestellung = service.erstelleBestellung(request, request.apothekenId());

            BestellungResponseDto storniert = service.storniereBestellung(bestellung.bestellId());

            assertThat(storniert.status()).isEqualTo("STORNIERT");
        }

        @Test
        @DisplayName("FAIL erwartet: Nicht gefundene Bestellung → Exception")
        void shouldThrowWhenNotFound() {



            assertThatThrownBy(() -> service.storniereBestellung("asdf")).isInstanceOf(BestellungNichtGefundenException.class);

        }
    }

    @Nested
    @DisplayName("holeAlleBestellungen()")
    class HoleAlleBestellungenTests {

        @Test
        @DisplayName("PASS: Leere Liste wenn keine Bestellungen vorhanden")
        void shouldReturnEmptyListWhenNoOrders() {

            BestellungListeDto liste = service.holeAlleBestellungen("asdf");

            assertThat(liste).isNotNull();
            assertThat(liste.anzahl()).isEqualTo(0);
            assertThat(liste.bestellungen()).isEmpty();
            // TODO: Test implementieren
            // Keine Bestellung erstellt
            // Erwarte: result.anzahl() == 0, result.bestellungen() ist leer
            // WICHTIG: Kein Exception — leere Liste ist korrekt!
        }

        @Test
        @DisplayName("PASS: Nur Bestellungen der richtigen Apotheke werden zurueckgegeben")
        void shouldReturnOnlyCorrectApothekenBestellungen() {
            BestellungRequestDto request1 = new BestellungRequestDto("APO-JENA-001", "Carboplatin", 5, "mg", null);
            service.erstelleBestellung(request1, request1.apothekenId());

            BestellungRequestDto request2 = new BestellungRequestDto("APO-JENA-001", "Ibu", 5, "mg", null);
            service.erstelleBestellung(request2, request2.apothekenId());

            BestellungRequestDto request3 = new BestellungRequestDto("APO-JENA-002", "Carboplatin", 5, "mg", null);
            service.erstelleBestellung(request3, request3.apothekenId());

            BestellungListeDto liste = service.holeAlleBestellungen("APO-JENA-001");

            assertThat(liste.anzahl()).isEqualTo(2);
            assertThat(liste.bestellungen()).allMatch(b -> b.apothekenId().equals("APO-JENA-001"));
        }


    }

}
