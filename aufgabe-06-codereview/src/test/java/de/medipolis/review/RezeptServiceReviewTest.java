package de.medipolis.review;

import de.medipolis.review.service.RezeptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import de.medipolis.review.repository.RezeptRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * ============================================================
 * AUFGABE: Tests fuer die gefixten Methoden schreiben
 * ============================================================
 *
 * Nachdem du alle Probleme gefixt hast, schreibe Tests
 * die beweisen dass deine Fixes korrekt sind.
 *
 * Fuehre aus:
 *   mvn test -Dtest=RezeptServiceReviewTest
 */
@ExtendWith(MockitoExtension.class)
class RezeptServiceReviewTest {

    @Mock
    private RezeptRepository repository;

    @InjectMocks
    private RezeptService service;

    @Nested
    @DisplayName("bestimmeDringlichkeit() — nach Fix von Problem #4")
    class DringlichkeitTests {

        @Test
        @DisplayName("PASS: 600mg → DRINGEND")
        void shouldReturnDringendForHighDose() {

            assertThat(service.bestimmeDringlichkeit(BigDecimal.valueOf(600))).isEqualTo("DRINGEND");
        }

        @Test
        @DisplayName("PASS: 250mg → NORMAL")
        void shouldReturnNormalForLowDose() {
            assertThat(service.bestimmeDringlichkeit(BigDecimal.valueOf(250))).isEqualTo("NORMAL");
        }

        @Test
        @DisplayName("GRENZWERT: Genau 500mg → DRINGEND (nicht NORMAL!)")
        void shouldReturnDringendForExactThreshold() {
            assertThat(service.bestimmeDringlichkeit(BigDecimal.valueOf(500))).isEqualTo("DRINGEND");
        }

        @Test
        @DisplayName("GRENZWERT: 499.99mg → NORMAL")
        void shouldReturnNormalJustBelowThreshold() {
            assertThat(service.bestimmeDringlichkeit(BigDecimal.valueOf(499.99))).isEqualTo("NORMAL");
        }
    }

    @Nested
    @DisplayName("check() — nach Fix von Problem #8")
    class CheckTests {

        @Test
        @DisplayName("PASS: Nach Umbenennung — Methode testet ob Dosierung kritisch ist")
        void shouldReturnTrueForCriticalDose() {
            assertThat(service.istUeberdosis(BigDecimal.valueOf(10000))).isTrue();
        }
    }
}
