package de.medipolis.support.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Alle Datenmodelle fuer das 2nd-Level-Support-Dashboard.
 */
public class SupportModels {

    /**
     * Ein einzelner Log-Eintrag — wie in Kibana/Graylog.
     * In Produktion kommen diese Eintraege aus Elasticsearch.
     * Hier simulieren wir sie in-memory.
     */
    public record LogEintrag(
            LocalDateTime zeitstempel,
            String level,           // INFO, WARN, ERROR
            String correlationId,
            String service,         // welcher Microservice
            String nachricht,
            String fehlerDetails    // nur bei ERROR — Stack Trace Zusammenfassung
    ) {}

    /**
     * Ergebnis einer Log-Analyse fuer eine Correlation-ID.
     * Simuliert was du in Kibana siehst wenn du nach einer ID filterst.
     */
    public record LogAnalyseErgebnis(
            String correlationId,
            List<LogEintrag> eintraege,
            String fehlerUrsache,       // Was ist schiefgegangen?
            String fehlerStufe,         // "VALIDIERUNG" | "DATENBANK" | "QUEUE" | "EXTERN"
            String empfehlung,          // Was soll der Support-Techniker tun?
            boolean kritisch            // Patientenversorgung betroffen?
    ) {}

    /**
     * System-Gesundheitsstatus — wie ein Kibana-Dashboard.
     */
    public record SystemStatus(
            String systemName,
            String status,              // "OK" | "WARNUNG" | "KRITISCH"
            int fehlerLetzteStunde,
            int warnungenLetzteStunde,
            String letzterFehler,
            LocalDateTime geprueftAm
    ) {}

    /**
     * Diagnose-Ergebnis nach automatischer Fehleranalyse.
     * Der Service analysiert Logs und gibt eine Diagnose.
     */
    public record DiagnoseErgebnis(
            String fehlerTyp,           // was ist kaputt
            String wahrscheinlicheUrsache,
            String sofortmassnahme,     // was der Support JETZT tun soll
            String permanenteFix,       // was danach gefixt werden muss
            boolean patientenBetroffen  // dringend oder nicht?
    ) {}

    /**
     * Support-Ticket das nach der Diagnose erstellt wird.
     */
    public record SupportTicket(
            String ticketId,
            String correlationId,
            String beschreibung,
            String prioritaet,          // "KRITISCH" | "HOCH" | "NORMAL"
            String zugewiesen,          // "Developer-on-Duty" | "Senior-Dev" | "Architekt"
            LocalDateTime erstelltAm
    ) {}
}
