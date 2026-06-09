package de.medipolis.ki.service;

import de.medipolis.ki.client.KiApiClient;
import de.medipolis.ki.model.Dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ============================================================
 * AUFGABE 5: Unit-Tests fuer KiAnalyseService mit Mockito
 * ============================================================
 *
 * Fuehre aus:
 *   mvn test -Dtest=KiAnalyseServiceTest
 *
 * Hier testest du den gesamten Analyse-Flow.
 * Der KiApiClient wird GEMOCKT — kein echter KI-API-Call!
 */
@ExtendWith(MockitoExtension.class)
class KiAnalyseServiceTest {

    @Mock
    private AnonymisierungsService anonymisierungsService;

    @Mock
    private KiApiClient kiApiClient;

    @InjectMocks
    private KiAnalyseService kiAnalyseService;

    private static final String CORRELATION_ID = "test-correlation-123";

    @Nested
    @DisplayName("parseKiAntwort() — KI-JSON parsen")
    class ParseTests {

        @Test
        @DisplayName("PASS: Gueltiges KI-JSON wird korrekt geparst")
        void shouldParseValidKiResponse() {
            // TODO: Test implementieren
            // Gegeben: gueltiges JSON von der KI:
            String kiJson = """
                    {
                      "medikament": "Carboplatin",
                      "dosierungMg": 450.0,
                      "einheit": "mg",
                      "diagnoseCode": "C34.1",
                      "hinweis": null
                    }
                    """;

            // Direkt parseKiAntwort() aufrufen (package-private):
            // ExtrahierteMedikamentendatenDto result =
            //     kiAnalyseService.parseKiAntwort(kiJson, CORRELATION_ID);
            //
            // Erwarte:
            //   result.medikament()    == "Carboplatin"
            //   result.dosierungMg()   == new BigDecimal("450.0")
            //   result.einheit()       == "mg"
            //   result.diagnoseCode()  == "C34.1"
            //   result.status()        == "ERFOLGREICH"
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: KI findet kein Medikament → NICHT_GEFUNDEN")
        void shouldReturnNichtGefundenWhenMedikamentNull() {
            // TODO: Test implementieren
            // KI antwortet mit null fuer Medikament:
            String kiJson = """
                    {
                      "medikament": null,
                      "dosierungMg": null,
                      "einheit": null,
                      "diagnoseCode": null,
                      "hinweis": "Kein Medikament im Text gefunden"
                    }
                    """;
            // Erwarte: result.status() == "NICHT_GEFUNDEN"
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: KI schickt ungueltige JSON → KI_FEHLER")
        void shouldReturnKiFehlerForInvalidJson() {
            // TODO: Test implementieren
            // KI halluziniert und schickt keinen gueltigen JSON:
            String ungueltigesJson = "Das ist kein JSON!";
            // Erwarte: result.status() == "KI_FEHLER"
            fail("AUFGABE 5: Implementiere diesen Test!");
        }
    }

    @Nested
    @DisplayName("analysiereArztbrief() — Gesamt-Flow")
    class AnalyseTests {

        @BeforeEach
        void setupMocks() {
            // AnonymisierungsService Mock immer zurueck:
            when(anonymisierungsService.anonymisiere(any()))
                    .thenReturn(new AnonymisierterArztbriefDto(
                            "anonymisierter text", "anon-id-123"));
        }

        @Test
        @DisplayName("PASS: Erfolgreicher Flow — KI antwortet korrekt")
        void shouldAnalyseSuccessfully() {
            // TODO: Test implementieren
            // KiApiClient Mock:
            // when(kiApiClient.analysiereText(any())).thenReturn("""
            //     {"medikament":"Carboplatin","dosierungMg":450.0,"einheit":"mg",
            //      "diagnoseCode":"C34.1","hinweis":null}""");
            //
            // Erwarte: result.status() == "ERFOLGREICH"
            // Verify: anonymisierungsService.anonymisiere() wurde aufgerufen
            // Verify: kiApiClient.analysiereText() wurde aufgerufen
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: KI nicht erreichbar → KI_FEHLER, kein Absturz!")
        void shouldHandleKiUnavailable() {
            // TODO: Test implementieren
            // when(kiApiClient.analysiereText(any())).thenReturn(null);
            // Erwarte: result.status() == "KI_FEHLER"
            // Service darf NICHT abstuerzen!
            fail("AUFGABE 5: Implementiere diesen Test!");
        }

        @Test
        @DisplayName("PASS: Anonymisierung wird IMMER VOR dem KI-Call aufgerufen")
        void shouldAlwaysAnonymizeBeforeKiCall() {
            // TODO: Test implementieren
            // Das ist der wichtigste Test dieser Aufgabe!
            // Beweise dass anonymisiere() VOR analysiereText() aufgerufen wird.
            //
            // Tipp: InOrder von Mockito nutzen:
            // InOrder inOrder = inOrder(anonymisierungsService, kiApiClient);
            // inOrder.verify(anonymisierungsService).anonymisiere(any());
            // inOrder.verify(kiApiClient).analysiereText(any());
            fail("AUFGABE 5: DAS ist der wichtigste Test — beweise die DSGVO-Reihenfolge!");
        }
    }
}
