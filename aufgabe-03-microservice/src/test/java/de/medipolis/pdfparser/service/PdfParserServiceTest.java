package de.medipolis.pdfparser.service;

import de.medipolis.pdfparser.exception.PdfParseException;
import de.medipolis.pdfparser.model.Dtos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * AUFGABE 4: Unit-Tests schreiben
 * ============================================================
 *
 * Fuehre nach deiner Implementierung aus:
 *   mvn test -Dtest=PdfParserServiceTest
 *
 * Alle Tests muessen gruen sein!
 *
 * Tipp: extrahiereFelder() ist package-private —
 * du kannst sie direkt testen ohne parsePdf() aufzurufen.
 */
class PdfParserServiceTest {

    private PdfParserService service;

    @BeforeEach
    void setUp() {
        service = new PdfParserService();
    }

    @Nested
    @DisplayName("extrahiereFelder()")
    class ExtrahiereFelderTests {

        @Test
        @DisplayName("PASS: Gueltiger PDF-Inhalt wird korrekt in Map umgewandelt")
        void shouldParseValidPdfContent() {

            String pdfInhalt = "PATIENT_ID:PAT-001;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg";
            Map<String, String> felder = service.extrahiereFelder(pdfInhalt);
            assertThat(felder.get("PATIENT_ID")).isEqualTo("PAT-001");
            assertThat(felder.get("MEDIKAMENT")).isEqualTo("Carboplatin");
            assertThat(felder.get("DOSIERUNG")).isEqualTo("450.5");
            assertThat(felder.get("EINHEIT")).isEqualTo("mg");

        }

        @Test
        @DisplayName("FAIL erwartet: Leerer PDF-Inhalt → IllegalArgumentException")
        void shouldThrowWhenPdfInhaltEmpty() {
            assertThatThrownBy(() -> service.extrahiereFelder("")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("FAIL erwartet: Null PDF-Inhalt → IllegalArgumentException")
        void shouldThrowWhenPdfInhaltNull() {
            assertThatThrownBy(() -> service.extrahiereFelder(null)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("parsePdf()")
    class ParsePdfTests {

        @Test
        @DisplayName("PASS: Gueltiges PDF wird erfolgreich verarbeitet")
        void shouldParseValidPdfSuccessfully() {

            Dtos.PdfParseRequestDto request = new Dtos.PdfParseRequestDto("PATIENT_ID:PAT-001;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg",
                    "klkrankenhaus");
            Dtos.ParseErgebnisDto parsed = service.parsePdf(request, UUID.randomUUID().toString());
            assertThat(parsed.patientId()).isEqualTo("PAT-001");
            assertThat(parsed.medikament()).isEqualTo("Carboplatin");
            assertThat(parsed.dosierungMg()).isEqualTo(new BigDecimal("450.5"));
            assertThat(parsed.einheit()).isEqualTo("mg");
            assertThat(parsed.status()).isEqualTo("ERFOLGREICH");
            assertThat(parsed.correlationId()).isNotNull();
            // TODO: Test implementieren
            // Gegeben: gueltiger Request mit allen Feldern
            // Erwarte:
            //   result.patientId()   == "PAT-001"
            //   result.medikament()  == "Carboplatin"
            //   result.dosierungMg() == new BigDecimal("450.5")
            //   result.einheit()     == "mg"
            //   result.status()      == "ERFOLGREICH"
            //   result.correlationId() ist nicht null
            //fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("FAIL erwartet: PATIENT_ID fehlt → PdfParseException")
        void shouldThrowWhenPatientIdMissing() {
            Dtos.PdfParseRequestDto request = new Dtos.PdfParseRequestDto("PATIENT_ID:;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg",
                    "klkrankenhaus");

            assertThatThrownBy(() -> service.parsePdf(request, UUID.randomUUID().toString()))
                    .isInstanceOf(PdfParseException.class);
        }

        @Test
        @DisplayName("FAIL erwartet: DOSIERUNG ist kein gueltiger Wert → PdfParseException")
        void shouldThrowWhenDosierungInvalid() {
            // TODO: Test implementieren
            // Gegeben: PDF mit DOSIERUNG:ABC (nicht parseable als Zahl)
            fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("FAIL erwartet: DOSIERUNG ist 0 → PdfParseException")
        void shouldThrowWhenDosierungZero() {
            // TODO: Test implementieren
            fail("AUFGABE 4: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("FAIL erwartet: EINHEIT fehlt → PdfParseException")
        void shouldThrowWhenEinheitMissing() {
            // TODO: Test implementieren
            fail("AUFGABE 4: Implementiere diesen Test!");
        }
    }
}
