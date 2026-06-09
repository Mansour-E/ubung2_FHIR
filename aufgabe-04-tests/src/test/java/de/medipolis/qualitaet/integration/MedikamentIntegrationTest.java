package de.medipolis.qualitaet.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import de.medipolis.qualitaet.model.Medikament;
import de.medipolis.qualitaet.repository.MedikamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ============================================================
 * AUFGABE 2: Integrationstests schreiben
 * ============================================================
 *
 * WICHTIG: Das ist ein INTEGRATIONSTEST.
 * - @SpringBootTest → echter Spring Context laeuft
 * - @Testcontainers → echter PostgreSQL Container (kein H2!)
 * - WireMock → externe Krankenkasse simulieren
 * - MockMvc → HTTP Requests an echten Controller schicken
 *
 * Warum kein H2?
 *   H2 bildet PostgreSQL-spezifische Features nicht korrekt ab.
 *   Testcontainers = echte PostgreSQL = gleich wie Produktion.
 *
 * Ausfuehren (Docker muss laufen!):
 *   mvn test -Dtest=MedikamentIntegrationTest
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class MedikamentIntegrationTest {

    // ✅ Echter PostgreSQL Container — kein H2!
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("medipolis_test")
            .withUsername("test")
            .withPassword("test");

    // ✅ WireMock simuliert die externe Krankenkasse
    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Testcontainers DB-Verbindung injizieren
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // WireMock URL als Krankenkasse-URL setzen
        registry.add("krankenkasse.url", wireMock::baseUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedikamentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    // ============================================================
    // AUFGABE 2A: Happy Path testen
    // ============================================================

    @Test
    @DisplayName("PASS: Gueltiges Medikament → 200 OK, in DB gespeichert, Krankenkasse GENEHMIGT")
    void shouldSaveMedikamentAndGetGenehmigt() throws Exception {
        // TODO: Test implementieren
        //
        // SCHRITT 1 — WireMock konfigurieren (Krankenkasse simulieren):
        //   wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
        //       .willReturn(WireMock.aResponse()
        //           .withStatus(200)
        //           .withBody("GENEHMIGT")));
        //
        // SCHRITT 2 — HTTP Request mit MockMvc schicken:
        //   String requestJson = """
        //       {
        //           "name": "Carboplatin",
        //           "dosierungMg": 450.00,
        //           "einheit": "mg",
        //           "patientId": "PAT-001"
        //       }
        //       """;
        //
        //   mockMvc.perform(post("/api/v1/medikamente")
        //       .contentType(MediaType.APPLICATION_JSON)
        //       .content(requestJson))
        //       .andExpect(status().isOk())
        //       .andExpect(jsonPath("$.name").value("Carboplatin"))
        //       .andExpect(jsonPath("$.krankenkasseStatus").value("GENEHMIGT"));
        //
        // SCHRITT 3 — In echter PostgreSQL DB pruefen:
        //   var alle = repository.findAll();
        //   assertThat(alle).hasSize(1);
        //   assertThat(alle.get(0).getName()).isEqualTo("Carboplatin");
        fail("AUFGABE 2A: Implementiere diesen Test!");
    }

    @Test
    @DisplayName("PASS: Fehlende Pflichtfelder → 400 Bad Request")
    void shouldReturn400WhenFieldsMissing() throws Exception {
        // TODO: Test implementieren
        // Request ohne "name" Feld schicken
        // Erwarte: status().isBadRequest()
        // Pruefe: repository.findAll() ist leer (nichts gespeichert)
        fail("AUFGABE 2A: Implementiere diesen Test!");
    }

    // ============================================================
    // AUFGABE 2B: Fehlerszenarien mit WireMock testen
    // ============================================================

    @Test
    @DisplayName("PASS: Krankenkasse gibt 500 zurueck → Service gibt UNBEKANNT zurueck")
    void shouldHandleKrankenkasse500Error() throws Exception {
        // TODO: Test implementieren
        //
        // WireMock gibt 500 zurueck:
        //   wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
        //       .willReturn(WireMock.aResponse()
        //           .withStatus(500)));
        //
        // Erwarte: HTTP 200 OK (der Service selbst faellt nicht aus!)
        // Erwarte: krankenkasseStatus = "UNBEKANNT"
        // (Der KrankenkasseClient faengt den Fehler intern ab)
        fail("AUFGABE 2B: Implementiere diesen Test!");
    }

    @Test
    @DisplayName("PASS: Krankenkasse antwortet nach 3 Sekunden → UNBEKANNT (Timeout)")
    void shouldHandleKrankenkasseTimeout() throws Exception {
        // TODO: Test implementieren
        //
        // WireMock simuliert Timeout mit Delay:
        //   wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
        //       .willReturn(WireMock.aResponse()
        //           .withStatus(200)
        //           .withFixedDelay(3000)  // 3 Sekunden Verzoegerung
        //           .withBody("GENEHMIGT")));
        //
        // Erwarte: HTTP 200 OK (Service faellt nicht aus)
        // Erwarte: krankenkasseStatus = "UNBEKANNT" oder "GENEHMIGT"
        //          (abhaengig vom RestTemplate Timeout-Setting)
        fail("AUFGABE 2B: Implementiere diesen Test!");
    }

    @Test
    @DisplayName("PASS: Krankenkasse gibt ABGELEHNT → korrekt im Response")
    void shouldReturnAbgelehntFromKrankenkasse() throws Exception {
        // TODO: Test implementieren
        // WireMock gibt "ABGELEHNT" zurueck
        // Erwarte: jsonPath("$.krankenkasseStatus").value("ABGELEHNT")
        fail("AUFGABE 2B: Implementiere diesen Test!");
    }

    // ============================================================
    // AUFGABE 2C: Datenbank-Persistenz pruefen
    // ============================================================

    @Test
    @DisplayName("PASS: Zwei Medikamente werden korrekt in PostgreSQL gespeichert")
    void shouldPersistMultipleMedikamente() throws Exception {
        // TODO: Test implementieren
        //
        // WireMock fuer beide Requests konfigurieren
        //
        // Zwei POST-Requests schicken:
        //   1. Carboplatin fuer PAT-001
        //   2. Methotrexat fuer PAT-002
        //
        // Pruefen:
        //   assertThat(repository.findAll()).hasSize(2);
        //   assertThat(repository.findByPatientId("PAT-001")).hasSize(1);
        //   assertThat(repository.findByPatientId("PAT-002")).hasSize(1);
        fail("AUFGABE 2C: Implementiere diesen Test!");
    }
}
