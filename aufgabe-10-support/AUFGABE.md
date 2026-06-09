# Aufgabe 10 — 2nd-Level-Support Dashboard

## Das Szenario (echter Support-Fall)

Es ist Dienstagmorgen 08:15 Uhr.
Die Apotheke Jena-Nord meldet: "Das System steht! Wir koennen
keine Etiketten drucken. Krebspatient wartet auf seine Infusion!"

Du bist heute Developer-on-Duty (DoD).

```
Was du tust:

Schritt 1 — Scope eingrenzen:
    GET /api/v1/support/fehler/uebersicht
    → Nur integration-service hat Fehler → kein globaler Ausfall

Schritt 2 — Correlation-ID holen + Logs analysieren:
    GET /api/v1/support/logs/KRISE-2026-001
    → 6 Log-Eintraege sichtbar: INFO, INFO, ERROR, WARN, ERROR

Schritt 3 — Automatische Diagnose:
    GET /api/v1/support/diagnose/KRISE-2026-001
    → Fehlertyp: VALIDIERUNG
    → Ursache: NullPointerException — Feld 'Dosierung' fehlt
    → Empfehlung: XML-Schema Krankenhaus Jena pruefen

Schritt 4 — Sofortmassnahme:
    Krankenhaus hat XML-Format geaendert (v1 → v2)
    Feld 'Dosierung' umbenannt zu 'DosierungMg'
    Hotfix: Validierung temporaer anpassen

Schritt 5 — Ticket erstellen:
    POST /api/v1/support/ticket/KRISE-2026-001
    → TICKET-KRITISCH erstellt, Developer-on-Duty + Senior-Dev

Schritt 6 — Apotheker informieren (ohne IT-Kauderwelsch):
    "Das Problem ist identifiziert. Das Krankenhaus hat ihr
     Dateiformat geaendert. Wir haben einen temporaeren Fix
     eingespielt. Sie koennen in ca. 10 Minuten wieder drucken."
```

---

## Deine Aufgaben

### AUFGABE 1 — DiagnoseService.java
Implementiere 5 Methoden:
- analysiereCorrelationId() → Logs holen + analysieren
- bestimmeFehlerTyp()       → VALIDIERUNG/DB/QUEUE/EXTERN erkennen
- generiereEmpfehlung()     → Konkrete Massnahmen pro Fehlertyp
- erstelleSupportTicket()   → Ticket mit Prioritaet und Zuweisung
- holeSystemStatus()        → Status OK/WARNUNG/KRITISCH

### AUFGABE 2 — SupportController.java
Implementiere 6 Endpunkte:
- GET /logs/{correlationId}       → Logs nach ID
- GET /diagnose/{correlationId}   → Automatische Diagnose
- GET /fehler/letzte/{minuten}    → Fehler der letzten N Minuten
- GET /status/{serviceName}       → System-Status
- GET /fehler/uebersicht          → Fehler pro Service
- POST /ticket/{correlationId}    → Support-Ticket erstellen

### AUFGABE 3 — DiagnoseServiceTest.java
Schreibe 13 Unit-Tests:
- 6 Tests fuer bestimmeFehlerTyp()
- 4 Tests fuer generiereEmpfehlung()
- 3 Tests fuer analysiereCorrelationId() + erstelleSupportTicket()

---

## Nach Implementierung testen

# Service starten:
mvn spring-boot:run

# Den echten Support-Fall durchspielen:

# 1. Gesamt-Uebersicht:
curl http://localhost:8088/api/v1/support/fehler/uebersicht

# 2. Logs fuer den Krisenfall:
curl http://localhost:8088/api/v1/support/logs/KRISE-2026-001

# 3. Automatische Diagnose:
curl http://localhost:8088/api/v1/support/diagnose/KRISE-2026-001

# 4. System-Status:
curl http://localhost:8088/api/v1/support/status/integration-service

# 5. Ticket erstellen:
curl -X POST http://localhost:8088/api/v1/support/ticket/KRISE-2026-001

# 6. Alle Fehler letzte 30 Minuten:
curl http://localhost:8088/api/v1/support/fehler/letzte/30

# 7. Spring Actuator Health (wie in Produktion):
curl http://localhost:8088/actuator/health

---

## Was der Tech-Lead fragt

"Wie gehst du vor wenn ein kritisches Ticket reinkommt?"
→ Top-Down: Scope eingrenzen → Correlation-ID suchen →
  Fehlertyp identifizieren → Sofortmassnahme →
  Apotheker informieren → permanentes Ticket.

"Was ist eine Correlation-ID und warum ist sie wichtig?"
→ Eine UUID die jeden einzelnen Request eindeutig identifiziert
  und in JEDEM Log-Eintrag mitgeschrieben wird.
  Ohne sie: du weisst nicht welche Logs zu welchem
  fehlerhaften Request gehoeren.
  Mit ihr: du filterst in Kibana und siehst alles.

"Wie kommunizierst du mit dem gestressten Apotheker?"
→ Ruhig bleiben. Sofort ein Update schicken:
  "Ich habe das Problem. Gebe dir in 20 Minuten ein Update."
  Kein IT-Fachjargon. Fortschritt kommunizieren.
  Erst Workaround, dann Root-Cause-Analyse.

"Was ist Developer-on-Duty?"
→ Rotierendes Prinzip: Ein Entwickler pro Sprint
  ist primaer fuer Support zustaendig.
  Rest des Teams kann ungestoert an Features arbeiten.
  Schuetzt alle vor Context-Switching.

"Warum Structured Logging mit MDC?"
→ In einem Thread-Pool teilen sich Requests dieselben Threads.
  Ohne MDC: Logs verschiedener Requests vermischen sich.
  Mit MDC + correlationId: Jeder Log-Eintrag hat die richtige
  ID — du kannst jeden Request in Kibana einzeln nachverfolgen.
