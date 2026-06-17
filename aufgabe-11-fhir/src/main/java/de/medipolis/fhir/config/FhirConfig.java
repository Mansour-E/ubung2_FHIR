package de.medipolis.fhir.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FHIR Konfiguration.
 *
 * FhirContext ist teuer zu erstellen — deshalb als @Bean (Singleton).
 * Einmal erstellt, ueberall injiziert.
 *
 * Wir nutzen FHIR R4 — der aktuelle Standard in Deutschland.
 * R4 wird von ISiK, KBV und Gematik verwendet.
 */
@Configuration
public class FhirConfig {

    /**
     * FHIR R4 Context — der Kern von HAPI FHIR.
     * Alle Parser und Encoder werden hierueber erstellt.
     */
    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }

    /**
     * JSON Parser — konvertiert FHIR Objekte zu JSON und zurueck.
     * Content-Type: application/fhir+json
     */
    @Bean
    public IParser fhirJsonParser(FhirContext fhirContext) {
        return fhirContext.newJsonParser().setPrettyPrint(true);
    }
}
