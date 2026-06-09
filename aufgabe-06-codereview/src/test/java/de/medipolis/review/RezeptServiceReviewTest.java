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
            // TODO: Test schreiben
            fail("Implementiere nach dem Fix von Problem #4!");
        }

        @Test
        @DisplayName("PASS: 250mg → NORMAL")
        void shouldReturnNormalForLowDose() {
            // TODO: Test schreiben
            fail("Implementiere nach dem Fix von Problem #4!");
        }

        @Test
        @DisplayName("GRENZWERT: Genau 500mg → DRINGEND (nicht NORMAL!)")
        void shouldReturnDringendForExactThreshold() {
            // TODO: Test schreiben
            // Das ist der klassische Grenzwert-Test!
            fail("Implementiere nach dem Fix von Problem #4!");
        }

        @Test
        @DisplayName("GRENZWERT: 499.99mg → NORMAL")
        void shouldReturnNormalJustBelowThreshold() {
            // TODO: Test schreiben
            fail("Implementiere nach dem Fix von Problem #4!");
        }
    }

    @Nested
    @DisplayName("check() — nach Fix von Problem #8")
    class CheckTests {

        @Test
        @DisplayName("PASS: Nach Umbenennung — Methode testet ob Dosierung kritisch ist")
        void shouldReturnTrueForCriticalDose() {
            // TODO: Test schreiben
            // Nach deinem Fix sollte die Methode einen klaren Namen haben
            // z.B. istKritischeDosierung()
            fail("Implementiere nach dem Fix von Problem #8!");
        }
    }
}
