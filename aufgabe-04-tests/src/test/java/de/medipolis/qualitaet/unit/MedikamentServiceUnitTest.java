package de.medipolis.qualitaet.unit;

import de.medipolis.qualitaet.client.KrankenkasseClient;
import de.medipolis.qualitaet.model.Dtos.MedikamentRequestDto;
import de.medipolis.qualitaet.model.Dtos.MedikamentResponseDto;
import de.medipolis.qualitaet.model.Medikament;
import de.medipolis.qualitaet.repository.MedikamentRepository;
import de.medipolis.qualitaet.service.MedikamentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ============================================================
 * AUFGABE 1: Unit-Tests schreiben mit Mockito
 * ============================================================
 *
 * WICHTIG: Das ist ein UNIT-TEST.
 * - Kein Spring Context (@SpringBootTest)
 * - Kein Docker, keine echte DB
 * - Alles was extern ist wird GEMOCKT (simuliert)
 * - Testet nur die reine Business-Logik
 *
 * @ExtendWith(MockitoExtension.class) — aktiviert Mockito
 * @Mock — erstellt eine simulierte Version der Klasse
 * @InjectMocks — erstellt den echten Service mit den Mocks injiziert
 *
 * Wie funktioniert Mockito?
 *   when(repository.save(any())).thenReturn(gespeichertesMedikament);
 *   → "Wenn save() aufgerufen wird, gib dieses Objekt zurueck"
 *
 * Ausfuehren:
 *   mvn test -Dtest=MedikamentServiceUnitTest
 */
@ExtendWith(MockitoExtension.class)
class MedikamentServiceUnitTest {

    @Mock
    private MedikamentRepository repository;

    @Mock
    private KrankenkasseClient krankenkasseClient;

    @InjectMocks
    private MedikamentService medikamentService;

    // ============================================================
    // AUFGABE 1A: speichereUndPruefe() testen
    // ============================================================

    @Nested
    @DisplayName("speichereUndPruefe() — mit gemockter DB und Krankenkasse")
    class SpeichereUndPruefeTests {

        @Test
        @DisplayName("PASS: Gueltiges Medikament wird gespeichert und Krankenkasse GENEHMIGT")
        void shouldSaveAndReturnGenehmigt(){

            MedikamentRequestDto request = new MedikamentRequestDto("Carboplatin" , BigDecimal.valueOf(450), "mg", "PAT-001");

            Medikament gespeichert = Medikament.builder()
                    .id(1L)
                    .name("Carboplatin")
                    .dosierungMg(new BigDecimal("450.00"))
                    .einheit("mg")
                    .patientId("PAT-001")
                    .status(Medikament.Status.NEU)
                    .build();

            when(repository.save(any())).thenReturn(gespeichert);
            when(krankenkasseClient.pruefeGenehmigung(any(),any())).thenReturn("GENEHMIGT");

            MedikamentResponseDto result = medikamentService.speichereUndPruefe(request);

            assertThat(result.name()).isEqualTo("Carboplatin");
            assertThat(result.krankenkasseStatus()).isEqualTo("GENEHMIGT");
            assertThat(result.id()).isEqualTo(1L);

            verify(repository , times(1)).save(any());
            verify(krankenkasseClient,times(1)).pruefeGenehmigung(any(),any());
            // TODO: Test implementieren
            //
            // SCHRITT 1 — Arrange (Vorbereitung):
            // Erstelle einen gueltigen MedikamentRequestDto:
            //   name="Carboplatin", dosierungMg=450.0, einheit="mg", patientId="PAT-001"
            //
            // Erstelle ein Medikament-Objekt das die DB zurueckgeben soll:
            //   Medikament gespeichert = Medikament.builder()
            //       .id(1L).name("Carboplatin").dosierungMg(new BigDecimal("450.0"))
            //       .einheit("mg").patientId("PAT-001").status(Medikament.Status.NEU).build();
            //
            // Definiere was die Mocks zurueckgeben sollen:
            //   when(repository.save(any())).thenReturn(gespeichert);
            //   when(krankenkasseClient.pruefeGenehmigung(any(), any())).thenReturn("GENEHMIGT");
            //
            // SCHRITT 2 — Act (Ausfuehren):
            //   MedikamentResponseDto result = medikamentService.speichereUndPruefe(request);
            //
            // SCHRITT 3 — Assert (Pruefen):
            //   assertThat(result.name()).isEqualTo("Carboplatin");
            //   assertThat(result.krankenkasseStatus()).isEqualTo("GENEHMIGT");
            //   assertThat(result.id()).isEqualTo(1L);
            //
            // SCHRITT 4 — Verify (War der Mock wirklich aufgerufen?):
            //   verify(repository, times(1)).save(any());
            //   verify(krankenkasseClient, times(1)).pruefeGenehmigung(any(), any());

        }

        @Test
        @DisplayName("PASS: Krankenkasse gibt ABGELEHNT zurueck")
        void shouldReturnAbgelehntWhenKrankenkasseAbgelehnt() {

            MedikamentRequestDto request = new MedikamentRequestDto("Carboplatin" , BigDecimal.valueOf(450), "mg", "PAT-001");

            Medikament gespeichert = Medikament.builder()
                    .id(1L)
                    .name("Carboplatin")
                    .dosierungMg(new BigDecimal("450.00"))
                    .einheit("mg")
                    .patientId("PAT-001")
                    .status(Medikament.Status.NEU)
                    .build();

            when(repository.save(any())).thenReturn(gespeichert);
            when(krankenkasseClient.pruefeGenehmigung(any(),any())).thenReturn("ABGELEHNT");


            MedikamentResponseDto result = medikamentService.speichereUndPruefe(request);

            assertThat(result.krankenkasseStatus()).isEqualTo("ABGELEHNT");

            // TODO: Test implementieren
            // Gleicher Aufbau wie oben aber:
            //   when(krankenkasseClient.pruefeGenehmigung(any(), any())).thenReturn("ABGELEHNT");
            // Erwarte: result.krankenkasseStatus() == "ABGELEHNT"

        }

        @Test
        @DisplayName("PASS: Krankenkasse nicht erreichbar → UNBEKANNT")
        void shouldReturnUnbekanntWhenKrankenkasseUnavailable() {


            MedikamentRequestDto request = new MedikamentRequestDto("Carboplatin", BigDecimal.valueOf(450), "mg", "PAT-001");

            Medikament gespeichert = Medikament.builder()
                    .id(1L)
                    .name("Carboplatin")
                    .dosierungMg(new BigDecimal("450.00"))
                    .einheit("mg")
                    .patientId("PAT-001")
                    .status(Medikament.Status.NEU)
                    .build();

            when(repository.save(any())).thenReturn(gespeichert);
            when(krankenkasseClient.pruefeGenehmigung(any(), any())).thenThrow(new RuntimeException("nicht erreichbar"));

            MedikamentResponseDto result = medikamentService.speichereUndPruefe(request);

            assertThat(result.krankenkasseStatus()).isEqualTo("UNBEKANNT");
            // TODO: Test implementieren
            // when(krankenkasseClient.pruefeGenehmigung(any(), any())).thenReturn("UNBEKANNT");

        }

        @Test
        @DisplayName("FAIL erwartet: Dosierung = 0 → IllegalArgumentException")
        void shouldThrowWhenDosierungZero() {

            MedikamentRequestDto request = new MedikamentRequestDto("Carboplatin", BigDecimal.valueOf(0), "mg", "PAT-001");




            assertThatThrownBy(() -> medikamentService.speichereUndPruefe(request)).isInstanceOf(IllegalArgumentException.class);

            verify(repository, never()).save(any());

            // TODO: Test implementieren
            // Erstelle Request mit dosierungMg = 0
            // assertThatThrownBy(...).isInstanceOf(IllegalArgumentException.class)
            // WICHTIG: Verify dass repository.save() NIE aufgerufen wurde!
            //   verify(repository, never()).save(any());

        }

        @Test
        @DisplayName("PASS: repository.save() wird genau einmal aufgerufen")
        void shouldCallRepositorySaveExactlyOnce() {

            Medikament gespeichert = Medikament.builder()
                    .id(1L)
                    .name("Carboplatin")
                    .dosierungMg(new BigDecimal("450.00"))
                    .einheit("mg")
                    .patientId("PAT-001")
                    .status(Medikament.Status.NEU)
                    .build();

            when(repository.save(any())).thenReturn(gespeichert);

            MedikamentRequestDto request = new MedikamentRequestDto("Carboplatin", BigDecimal.valueOf(450), "mg", "PAT-001");
            medikamentService.speichereUndPruefe(request);

            verify(repository, times(1)).save(any());
            // TODO: Test implementieren
            // Pruefe mit verify(repository, times(1)).save(any())

        }
    }

    // ============================================================
    // AUFGABE 1B: berechneGewichtsDosis() testen — reine Logik
    // ============================================================

    @Nested
    @DisplayName("berechneGewichtsDosis() — reine Business-Logik, kein Mock noetig")
    class BerechneGewichtsDosisTests {

        @Test
        @DisplayName("PASS: Normale Berechnung — 300mg bei 60kg")
        void shouldCalculateCorrectDose() {

            assertThat(medikamentService.berechneGewichtsDosis(new BigDecimal("300.00"), BigDecimal.valueOf(60))).isEqualTo(BigDecimal.valueOf(300).setScale(2, java.math.RoundingMode.HALF_UP));

            // TODO: Test implementieren
            // BigDecimal result = medikamentService.berechneGewichtsDosis(
            //     new BigDecimal("300"), new BigDecimal("60"));
            // assertThat(result).isEqualByComparingTo(new BigDecimal("300.00"));

        }

        @Test
        @DisplayName("FAIL erwartet: Basisdosis = 0 → IllegalArgumentException")
        void shouldThrowWhenBasisdosisZero() {

            assertThatThrownBy(() -> medikamentService.berechneGewichtsDosis(BigDecimal.ZERO , BigDecimal.valueOf(60))).isInstanceOf(IllegalArgumentException.class);
            // TODO: Test implementieren
        }

        @Test
        @DisplayName("FAIL erwartet: Gewicht negativ → IllegalArgumentException")
        void shouldThrowWhenGewichtNegative() {
            // TODO: Test implementieren
            assertThatThrownBy(() -> medikamentService.berechneGewichtsDosis(BigDecimal.valueOf(300), BigDecimal.valueOf(-20))).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("FAIL erwartet: Basisdosis null → IllegalArgumentException")
        void shouldThrowWhenBasisdosisNull() {
            // TODO: Test implementieren
            assertThatThrownBy(() -> medikamentService.berechneGewichtsDosis(null, BigDecimal.valueOf(60))).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
