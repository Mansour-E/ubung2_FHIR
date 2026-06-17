package de.medipolis.fhir;

import de.medipolis.fhir.mapper.RezeptFhirMapper;
import de.medipolis.fhir.model.Rezept;
import org.hl7.fhir.r4.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * DEINE TEST-AUFGABE — FhirRezeptMapperTest
 * ============================================================
 *
 * Schreibe Tests die beweisen dass dein Mapper korrekt funktioniert.
 * Kein Spring Context noetig — reiner Unit-Test!
 *
 * Ausfuehren:
 *   mvn test -Dtest=FhirRezeptMapperTest
 */
class FhirRezeptMapperTest {

    private RezeptFhirMapper mapper;
    private Rezept testRezept;

    @BeforeEach
    void setUp() {
        mapper = new RezeptFhirMapper();

        // Standard Testdaten — benutze dieses Objekt in deinen Tests!
        testRezept = Rezept.builder()
                .id(1L)
                .patientId("PAT-001")
                .patientName("Max Mustermann")
                .medikament("Carboplatin")
                .dosierungMg(new BigDecimal("450.00"))
                .einheit("mg")
                .arztName("Dr. Mueller")
                .pzn("12345678")
                .status("NEU")
                .build();
    }

    @Nested
    @DisplayName("toMedicationRequest() — Einzelnes Rezept zu FHIR")
    class ToMedicationRequestTests {

        @Test
        @DisplayName("TODO 5: Rezept wird zu MedicationRequest gemappt")
        void shouldMapRezeptToMedicationRequest() {
            // TODO: Implementiere!
            // 1. mapper.toMedicationRequest(testRezept) aufrufen
            // 2. Ergebnis ist nicht null
            // 3. ID ist "1"
            //
            // Tipp:
            // MedicationRequest mr = mapper.toMedicationRequest(testRezept);
            // assertThat(mr).isNotNull();
            // assertThat(mr.getId()).isEqualTo("1");
            fail("TODO 5: Implementiere shouldMapRezeptToMedicationRequest()!");
        }

        @Test
        @DisplayName("TODO 6: Deutsches ISiK Profil ist gesetzt")
        void shouldSetDeutschesISiKProfil() {
            // TODO: Implementiere!
            // Pruefe ob meta.profile die ISiK URL enthaelt:
            // "https://gematik.de/fhir/isik/StructureDefinition/ISiKMedikationsVerordnung"
            //
            // Tipp:
            // MedicationRequest mr = mapper.toMedicationRequest(testRezept);
            // assertThat(mr.getMeta().getProfile())
            //     .anyMatch(url -> url.getValue().contains("ISiKMedikationsVerordnung"));
            fail("TODO 6: Implementiere shouldSetDeutschesISiKProfil()!");
        }

        @Test
        @DisplayName("TODO 7: Status ist ACTIVE und Intent ist ORDER")
        void shouldSetCorrectStatusAndIntent() {
            // TODO: Implementiere!
            // ISiK Profil schreibt vor:
            // status = ACTIVE
            // intent = ORDER
            //
            // Tipp:
            // assertThat(mr.getStatus())
            //     .isEqualTo(MedicationRequest.MedicationRequestStatus.ACTIVE);
            // assertThat(mr.getIntent())
            //     .isEqualTo(MedicationRequest.MedicationRequestIntent.ORDER);
            fail("TODO 7: Implementiere shouldSetCorrectStatusAndIntent()!");
        }

        @Test
        @DisplayName("TODO 8: Patient (subject) ist korrekt gesetzt")
        void shouldSetCorrectPatient() {
            // TODO: Implementiere!
            // subject.reference = "Patient/PAT-001"
            // subject.display = "Max Mustermann"
            //
            // Tipp:
            // assertThat(mr.getSubject().getReference()).isEqualTo("Patient/PAT-001");
            // assertThat(mr.getSubject().getDisplay()).isEqualTo("Max Mustermann");
            fail("TODO 8: Implementiere shouldSetCorrectPatient()!");
        }

        @Test
        @DisplayName("TODO 9: Dosierung ist korrekt gesetzt")
        void shouldSetCorrectDosierung() {
            // TODO: Implementiere!
            // dosageInstruction[0].doseAndRate[0].doseQuantity.value = 450.00
            // dosageInstruction[0].doseAndRate[0].doseQuantity.unit = "mg"
            //
            // Tipp:
            // Dosage dosage = mr.getDosageInstructionFirstRep();
            // Quantity quantity = dosage.getDoseAndRateFirstRep().getDoseQuantity();
            // assertThat(quantity.getValue()).isEqualByComparingTo(new BigDecimal("450.00"));
            // assertThat(quantity.getUnit()).isEqualTo("mg");
            fail("TODO 9: Implementiere shouldSetCorrectDosierung()!");
        }

        @Test
        @DisplayName("TODO 10: PZN Coding ist gesetzt wenn PZN vorhanden")
        void shouldSetPznCoding() {
            // TODO: Implementiere!
            // system = "http://fhir.de/CodeSystem/ifa/pzn"
            // code = "12345678"
            //
            // Tipp:
            // CodeableConcept med = (CodeableConcept) mr.getMedication();
            // Coding coding = med.getCodingFirstRep();
            // assertThat(coding.getSystem()).isEqualTo("http://fhir.de/CodeSystem/ifa/pzn");
            // assertThat(coding.getCode()).isEqualTo("12345678");
            fail("TODO 10: Implementiere shouldSetPznCoding()!");
        }

        @Test
        @DisplayName("TODO 10b: Kein Coding wenn PZN null ist")
        void shouldNotSetCodingWhenPznIsNull() {
            // TODO: Implementiere!
            // testRezept ohne PZN erstellen
            // med.getCoding() soll leer sein
            fail("TODO 10b: Implementiere shouldNotSetCodingWhenPznIsNull()!");
        }
    }

    @Nested
    @DisplayName("toBundle() — Liste zu FHIR Bundle")
    class ToBundleTests {

        @Test
        @DisplayName("TODO 11: Liste wird zu Bundle gemappt")
        void shouldMapListToBundle() {
            // TODO: Implementiere!
            // 1. List.of(testRezept, testRezept) erstellen
            // 2. mapper.toBundle() aufrufen
            // 3. bundle.getType() == Bundle.BundleType.SEARCHSET
            // 4. bundle.getTotal() == 2
            // 5. bundle.getEntry().size() == 2
            fail("TODO 11: Implementiere shouldMapListToBundle()!");
        }

        @Test
        @DisplayName("TODO 12: Leere Liste ergibt leeres Bundle")
        void shouldMapEmptyListToEmptyBundle() {
            // TODO: Implementiere!
            // bundle.getTotal() == 0
            // bundle.getEntry().isEmpty() == true
            fail("TODO 12: Implementiere shouldMapEmptyListToEmptyBundle()!");
        }
    }
}
