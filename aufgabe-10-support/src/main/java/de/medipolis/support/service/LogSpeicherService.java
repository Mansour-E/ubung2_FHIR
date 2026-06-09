package de.medipolis.support.service;

import de.medipolis.support.model.SupportModels.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ============================================================
 * DIESER SERVICE IST BEREITS FERTIG IMPLEMENTIERT
 * ============================================================
 *
 * Er simuliert einen Log-Speicher (wie Elasticsearch/Kibana).
 * Logs werden in-memory gespeichert und koennen durchsucht werden.
 *
 * In Produktion wuerde hier ein Elasticsearch-Client stehen.
 *
 * DEINE AUFGABE: Diesen Service verstehen und im
 * DiagnoseService und SupportController nutzen.
 */
@Service
public class LogSpeicherService {

    private static final Logger log = LoggerFactory.getLogger(LogSpeicherService.class);

    // Simulierter Log-Speicher (wie Elasticsearch Index)
    private final List<LogEintrag> logs = new ArrayList<>();

    public LogSpeicherService() {
        // Simulierte Logs beim Start laden
        ladeSimulierteKrisenLogs();
    }

    /**
     * Suche alle Logs fuer eine bestimmte correlationId.
     * Wie in Kibana: Filter by correlationId.
     */
    public List<LogEintrag> sucheNachCorrelationId(String correlationId) {
        return logs.stream()
                .filter(l -> correlationId.equals(l.correlationId()))
                .sorted(Comparator.comparing(LogEintrag::zeitstempel))
                .collect(Collectors.toList());
    }

    /**
     * Alle ERROR-Logs der letzten N Minuten.
     * Wie in Kibana: level:ERROR AND @timestamp > now-Nm
     */
    public List<LogEintrag> holeFehlerLetzteMinuten(int minuten) {
        LocalDateTime grenze = LocalDateTime.now().minusMinutes(minuten);
        return logs.stream()
                .filter(l -> "ERROR".equals(l.level()))
                .filter(l -> l.zeitstempel().isAfter(grenze))
                .sorted(Comparator.comparing(LogEintrag::zeitstempel).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Alle Logs fuer einen bestimmten Service.
     */
    public List<LogEintrag> sucheNachService(String serviceName) {
        return logs.stream()
                .filter(l -> serviceName.equals(l.service()))
                .sorted(Comparator.comparing(LogEintrag::zeitstempel).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Zaehlt Fehler pro Service in der letzten Stunde.
     */
    public Map<String, Long> fehlerProService() {
        LocalDateTime vorEinerStunde = LocalDateTime.now().minusHours(1);
        return logs.stream()
                .filter(l -> "ERROR".equals(l.level()))
                .filter(l -> l.zeitstempel().isAfter(vorEinerStunde))
                .collect(Collectors.groupingBy(LogEintrag::service, Collectors.counting()));
    }

    /**
     * Neuen Log-Eintrag hinzufuegen (simuliert Logstash-Eingang).
     */
    public void logge(String level, String correlationId, String service,
                      String nachricht, String fehlerDetails) {
        logs.add(new LogEintrag(
                LocalDateTime.now(), level, correlationId,
                service, nachricht, fehlerDetails
        ));
    }

    /**
     * Simulierte Krisenlogs — repraesentieren einen echten Support-Fall.
     *
     * SUPPORT-FALL: Apotheke Jena kann keine Etiketten drucken.
     * Correlation-ID des fehlerhaften Requests: KRISE-2026-001
     *
     * Als 2nd-Level-Support-Techniker wuerdest du diese Logs
     * in Kibana nach dieser Correlation-ID filtern.
     */
    private void ladeSimulierteKrisenLogs() {
        LocalDateTime basis = LocalDateTime.now().minusMinutes(15);

        // Normaler Request vorher — zum Vergleich
        logge("INFO",  "NORMAL-2026-000", "integration-service",
              "Verordnung empfangen von Krankenhaus Erfurt", null);
        logge("INFO",  "NORMAL-2026-000", "integration-service",
              "Validierung erfolgreich", null);
        logge("INFO",  "NORMAL-2026-000", "pharmacy-service",
              "Produktionsauftrag an Apotheke gesendet", null);

        // Der kritische fehlerhafte Request
        logge("INFO",  "KRISE-2026-001", "integration-service",
              "Verordnung empfangen von Krankenhaus Jena", null);
        logge("INFO",  "KRISE-2026-001", "integration-service",
              "XML-Parsing gestartet", null);
        logge("ERROR", "KRISE-2026-001", "integration-service",
              "XML-Validierung fehlgeschlagen",
              "NullPointerException: Element 'Dosierung' erwartet aber nicht gefunden. " +
              "Krankenhaus Jena hat XML-Format geaendert (v1 -> v2). " +
              "Feld 'Dosierung' umbenannt zu 'DosierungMg'.");
        logge("WARN",  "KRISE-2026-001", "integration-service",
              "Verordnung in Dead-Letter-Queue verschoben", null);
        logge("ERROR", "KRISE-2026-001", "pharmacy-service",
              "Kein Produktionsauftrag empfangen — Apotheke kann nicht drucken",
              "Timeout beim Warten auf Verordnung. Queue leer.");

        // Weitere aehnliche Fehler (selbes Muster — Krankenhaus Jena hat XML geaendert)
        logge("ERROR", "KRISE-2026-002", "integration-service",
              "XML-Validierung fehlgeschlagen",
              "NullPointerException: Element 'Dosierung' fehlt. Selbes Muster wie KRISE-001.");
        logge("ERROR", "KRISE-2026-003", "integration-service",
              "XML-Validierung fehlgeschlagen",
              "NullPointerException: Element 'Dosierung' fehlt.");

        // DB-Problem bei einem anderen Service
        logge("ERROR", "DB-FEHLER-001", "patient-service",
              "Datenbankverbindung verloren",
              "org.postgresql.util.PSQLException: Connection refused. " +
              "PostgreSQL-Server nicht erreichbar. Moeglicher Neustart.");
        logge("WARN",  "DB-FEHLER-001", "patient-service",
              "Retry-Versuch 1/3", null);
        logge("INFO",  "DB-FEHLER-001", "patient-service",
              "Datenbankverbindung wiederhergestellt nach Retry 2", null);

        log.info("Simulierte Support-Logs geladen: {} Eintraege", logs.size());
    }
}
