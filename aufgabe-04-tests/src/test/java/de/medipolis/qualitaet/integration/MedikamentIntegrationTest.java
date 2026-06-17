package de.medipolis.qualitaet.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/medipolis_integration",
        "spring.datasource.username=postgres",
        "spring.datasource.password=${DB_PASSWORD:9157}",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.baseline-on-migrate=true"
})
class MedikamentIntegrationTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void wireMockProperties(DynamicPropertyRegistry registry) {
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

    @Test
    @DisplayName("PASS: Gueltiges Medikament → 200 OK, in DB gespeichert, Krankenkasse GENEHMIGT")
    void shouldSaveMedikamentAndGetGenehmigt() throws Exception {

        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
                .willReturn(WireMock.aResponse()
                            .withStatus(200)
                            .withBody("GENEHMIGT")));

        String requestJson = """
                {
                    "name": "Carboplatin",
                    "dosierungMg": 450.00,
                    "einheit": "mg",
                    "patientId": "PAT-001"
                }
                """;

        mockMvc.perform(post("/api/v1/medikamente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carboplatin"))
                .andExpect(jsonPath("$.dosierungMg").value(450.00))
                .andExpect(jsonPath("$.krankenkasseStatus").value("GENEHMIGT"));

        var alle = repository.findAll();
        assertThat(alle).hasSize(1);
        assertThat(alle.getFirst().getName()).isEqualTo("Carboplatin");
    }

    @Test
    @DisplayName("PASS: Fehlende Pflichtfelder → 400 Bad Request, nichts in DB")
    void shouldReturn400WhenFieldsMissing() throws Exception {

        String requestJson = """
                {
                    "name": "Carboplatin",
                    "einheit": "mg",
                    "patientId": "PAT-001"
                }
                """;

        mockMvc.perform(post("/api/v1/medikamente").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest());

        assertThat(repository.findAll()).isEmpty();

    }

    @Test
    @DisplayName("PASS: Krankenkasse gibt 500 → Service gibt UNBEKANNT, kein Absturz!")
    void shouldHandleKrankenkasse500Error() throws Exception {

        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
                .willReturn(WireMock.aResponse().withStatus(500)));

        String requestJson = """
                {
                    "name": "Carboplatin",
                    "dosierungMg": 450.00,
                    "einheit": "mg",
                    "patientId": "PAT-001"
                }
                """;

        mockMvc.perform(post("/api/v1/medikamente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.krankenkasseStatus").value("UNBEKANNT"));
    }

    @Test
    @DisplayName("PASS: Krankenkasse gibt ABGELEHNT → korrekt im Response")
    void shouldReturnAbgelehntFromKrankenkasse() throws Exception {

        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody("ABGELEHNT")));

        String requestJson = """
                {
                    "name": "Carboplatin",
                    "dosierungMg": 450.00,
                    "einheit": "mg",
                    "patientId": "PAT-001"
                }
                """;

        mockMvc.perform(post("/api/v1/medikamente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carboplatin"))
                .andExpect(jsonPath("$.krankenkasseStatus").value("ABGELEHNT"));
    }

    @Test
    @DisplayName("PASS: Krankenkasse Timeout → UNBEKANNT")
    void shouldHandleKrankenkasseTimeout() throws Exception {

        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
                .willReturn(WireMock.aResponse()
                        .withFixedDelay(5000)
                        .withStatus(200)
                        .withBody("GENEHMIGT")));

        String requestJson = """
                {
                    "name": "Carboplatin",
                    "dosierungMg": 450.00,
                    "einheit": "mg",
                    "patientId": "PAT-001"
                }
                """;

        mockMvc.perform(post("/api/v1/medikamente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carboplatin"))
                .andExpect(jsonPath("$.krankenkasseStatus").value("UNBEKANNT"));


    }

    @Test
    @DisplayName("PASS: Zwei Medikamente werden korrekt in PostgreSQL gespeichert")
    void shouldPersistMultipleMedikamente() throws Exception {

        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody("GENEHMIGT")));


        List<String> requests = List.of("""
                {
                    "name": "Carboplatin",
                    "dosierungMg": 450.00,
                    "einheit": "mg",
                    "patientId": "PAT-001"
                }
                ""","""
                
                {
                    "name": "ibu",
                    "dosierungMg": 400.00,
                    "einheit": "mg",
                    "patientId": "PAT-001"
                }
                """);

        requests.forEach(request -> {
            try {
                mockMvc.perform(post("/api/v1/medikamente")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                        .andExpect(status().isOk());
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });

        assertThat(repository.findAll()).hasSize(2);
        assertThat(repository.findAll().getFirst().getName()).isEqualTo("Carboplatin");
        assertThat(repository.findAll().getLast().getName()).isEqualTo("ibu");
    }
}
