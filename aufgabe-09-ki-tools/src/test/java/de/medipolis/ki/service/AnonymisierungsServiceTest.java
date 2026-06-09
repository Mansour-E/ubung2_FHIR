package de.medipolis.ki.service;

import de.medipolis.ki.model.Dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * AUFGABE 4: Unit-Tests fuer AnonymisierungsService
 * ============================================================
 *
 * Fuehre aus:
 *   mvn test -Dtest=AnonymisierungsServiceTest
 *
 * Diese Tests pruefen das WICHTIGSTE in dieser Aufgabe:
 * Dass KEINE Patientendaten zur KI gelangen!
 */
class AnonymisierungsServiceTest {

    private AnonymisierungsService service;

    @BeforeEach
    void setUp() {
        service = new AnonymisierungsService();
    }

    @Nested
    @DisplayName("ersetzeNamen()")
    class NamenTests {

        @Test
        @DisplayName("PASS: 'Patient Max Mustermann' wird anonymisiert")
        void shouldAnonymizePatientName() {
            // TODO: Test implementieren
            // String result = service.ersetzeNamen("Patient Max Mustermann wurde aufgenommen");
            // assertThat(result).contains("[ANONYM]")
            // assertThat(result).doesNotContain("Max Mustermann")
            fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: 'Dr. Schmidt' wird anonymisiert")
        void shouldAnonymizeDoctorName() {
            // TODO: Test implementieren
            fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: Text ohne Namen bleibt unveraendert")
        void shouldLeaveTextWithoutNamesUnchanged() {
            // TODO: Test implementieren
            // Medikamentennamen sollen NICHT ersetzt werden!
            // "Carboplatin 450mg" → bleibt "Carboplatin 450mg"
            fail("AUFGABE 4: Implementiere diesen Test!");
        }
    }

    @Nested
    @DisplayName("ersetzeDaten()")
    class DatumTests {

        @Test
        @DisplayName("PASS: Datum DD.MM.YYYY wird ersetzt")
        void shouldAnonymizeDate() {
            // TODO: Test implementieren
            // "Aufnahme am 15.03.2026" → "Aufnahme am [DATUM]"
            fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: Mehrere Daten im Text werden alle ersetzt")
        void shouldAnonymizeMultipleDates() {
            // TODO: Test implementieren
            fail("AUFGABE 4: Implementiere diesen Test!");
        }
    }

    @Nested
    @DisplayName("ersetzeIds()")
    class IdTests {

        @Test
        @DisplayName("PASS: PAT-12345 wird anonymisiert")
        void shouldAnonymizePatientId() {
            // TODO: Test implementieren
            // "PatientenId: PAT-12345" → "PatientenId: PAT-[ID]"
            fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: Versicherungsnummer wird anonymisiert")
        void shouldAnonymizeInsuranceNumber() {
            // TODO: Test implementieren
            // Versicherungsnummer Format: 1 Buchstabe + 9 Zahlen, z.B. "A123456789"
            fail("AUFGABE 4: Implementiere diesen Test!");
        }
    }

    @Nested
    @DisplayName("anonymisiere() — Gesamt-Test")
    class GesamtTests {

        @Test
        @DisplayName("PASS: Kompletter Arztbrief wird vollstaendig anonymisiert")
        void shouldFullyAnonymizeArztbrief() {
            // TODO: Test implementieren
            String arztbrief = """
                    Patient Max Mustermann (PAT-99001, geb. 12.05.1965)
                    wurde am 15.03.2026 aufgenommen.
                    Behandelnder Arzt: Dr. Schmidt.
                    Diagnose: C34.1, Medikament: Carboplatin 450mg.
                    Versicherungsnummer: A123456789
                    """;

            // Erwarte:
            // - "Max Mustermann" nicht mehr im Text
            // - "PAT-99001" nicht mehr im Text
            // - "12.05.1965" nicht mehr im Text
            // - "15.03.2026" nicht mehr im Text
            // - "Dr. Schmidt" nicht mehr im Text
            // - "A123456789" nicht mehr im Text
            // - "Carboplatin" noch im Text (Medikament bleibt!)
            // - "C34.1" noch im Text (ICD-Code bleibt!)
            fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: AnonymisierungsId ist nicht null")
        void shouldGenerateAnonymisierungsId() {
            // TODO: Test implementieren
            fail("AUFGABE 4: Implementiere diesen Test!");
        }
    }
}
