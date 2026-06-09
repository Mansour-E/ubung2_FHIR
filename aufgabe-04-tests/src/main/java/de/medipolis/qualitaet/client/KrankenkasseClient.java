package de.medipolis.qualitaet.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client fuer die externe Krankenkassen-API.
 * Prueft ob ein Medikament von der Krankenkasse genehmigt wird.
 *
 * Diese Klasse ist bereits fertig implementiert.
 * Im Integrationstest wirst du sie mit WireMock simulieren.
 */
@Component
public class KrankenkasseClient {

    private static final Logger log = LoggerFactory.getLogger(KrankenkasseClient.class);

    private final RestTemplate restTemplate;
    private final String krankenkasseUrl;

    public KrankenkasseClient(
            RestTemplate restTemplate,
            @Value("${krankenkasse.url:http://localhost:8090}") String krankenkasseUrl) {
        this.restTemplate = restTemplate;
        this.krankenkasseUrl = krankenkasseUrl;
    }

    /**
     * Fragt die Krankenkasse ob das Medikament genehmigt wird.
     * Gibt "GENEHMIGT" oder "ABGELEHNT" zurueck.
     */
    public String pruefeGenehmigung(String medikamentName, String patientId) {
        try {
            log.info("Pruefe Genehmigung bei Krankenkasse fuer Medikament-Kategorie");
            String url = krankenkasseUrl + "/api/genehmigung?medikament=" + medikamentName;
            String antwort = restTemplate.getForObject(url, String.class);
            return antwort != null ? antwort : "ABGELEHNT";
        } catch (Exception e) {
            log.error("Krankenkasse nicht erreichbar: {}", e.getMessage());
            return "UNBEKANNT";
        }
    }
}
