package de.medipolis.ki.client;

import de.medipolis.ki.model.Dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Client fuer die externe KI-API (simuliert OpenAI-Format).
 *
 * BEREITS FERTIG IMPLEMENTIERT — du musst ihn nur verstehen
 * und im KiAnalyseService nutzen.
 *
 * In echten Projekten:
 * - Echter OpenAI API Key in Vault gespeichert
 * - Nie im Code, nie in application.yml committed
 * - Hier: ${KI_API_KEY} aus Umgebungsvariable
 */
@Component
public class KiApiClient {

    private static final Logger log = LoggerFactory.getLogger(KiApiClient.class);

    private final RestTemplate restTemplate;
    private final String kiApiUrl;
    private final String kiApiKey;
    private final String kiModel;

    public KiApiClient(
            RestTemplate restTemplate,
            @Value("${ki.api.url}") String kiApiUrl,
            @Value("${ki.api.key}") String kiApiKey,
            @Value("${ki.api.model}") String kiModel) {
        this.restTemplate = restTemplate;
        this.kiApiUrl = kiApiUrl;
        this.kiApiKey = kiApiKey;
        this.kiModel = kiModel;
    }

    /**
     * Schickt den anonymisierten Text zur KI und holt die Antwort.
     * Gibt null zurueck wenn KI nicht erreichbar.
     */
    public String analysiereText(String anonymisierterText) {
        try {
            log.info("KI-API Call wird gesendet (anonymisierter Text)");

            // System-Prompt — erklaert der KI ihre Aufgabe
            String systemPrompt = """
                    Du bist ein medizinischer Daten-Extraktor.
                    Extrahiere aus dem Arztbrief folgende Informationen als JSON:
                    {
                      "medikament": "Name des Medikaments oder null",
                      "dosierungMg": Zahl oder null,
                      "einheit": "mg/ml/Stueck oder null",
                      "diagnoseCode": "ICD-10 Code oder null",
                      "hinweis": "Hinweis wenn etwas unklar ist oder null"
                    }
                    Antworte NUR mit dem JSON-Objekt, nichts anderes.
                    Patientendaten wurden bereits anonymisiert.
                    """;

            KiApiRequestDto requestBody = new KiApiRequestDto(
                    kiModel,
                    List.of(
                            new KiNachrichtDto("system", systemPrompt),
                            new KiNachrichtDto("user", anonymisierterText)
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(kiApiKey);  // KI_API_KEY aus Umgebungsvariable

            HttpEntity<KiApiRequestDto> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<KiApiResponseDto> response = restTemplate.exchange(
                    kiApiUrl + "/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    KiApiResponseDto.class
            );

            if (response.getBody() != null &&
                !response.getBody().choices().isEmpty()) {
                return response.getBody().choices().get(0).message().content();
            }

            return null;

        } catch (Exception e) {
            log.error("KI-API nicht erreichbar: {}", e.getMessage());
            return null;
        }
    }
}
