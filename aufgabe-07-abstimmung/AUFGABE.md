# Aufgabe 07 — Abstimmung mit Anwendern: Design First + API-Contract

## Das Szenario (wie im echten Job bei CONVALES)

Apotheker Klaus Meier aus Jena kommt zu dir und sagt:
"Ich brauche eine Moeglichkeit meine Bestellungen zu verwalten."

Du hoerst zu, stellst Rueckfragen, und uebersetzt das in einen
technischen API-Contract — BEVOR eine einzige Zeile Code geschrieben wird.

Das nennt sich "Design First" — und genau das erwartet CONVALES.

---

## Was du in dieser Aufgabe lernst

1. API-Contract definieren (OpenAPI/Swagger Annotationen)
2. Fehlermeldungen fuer Nicht-IT-Anwender formulieren
3. REST-Design-Entscheidungen begruenden (201 vs 200, 404 vs leere Liste)
4. "Design First" Ansatz in der Praxis

---

## Deine Aufgaben

### AUFGABE 1 — BestellungsService.java
Implementiere 4 Methoden:
- erstelleBestellung()      → Bestellung anlegen, Lieferzeit berechnen
- holeBestellungStatus()    → Status abfragen, Exception wenn nicht gefunden
- holeAlleBestellungen()    → Liste filtern, leere Liste wenn keine vorhanden
- storniereBestellung()     → Nur wenn Status "EINGEGANGEN"

### AUFGABE 2 — BestellungsController.java
Implementiere 4 Endpunkte:
- POST   /{apothekenId}/bestellungen         → 201 Created
- GET    /{apothekenId}/bestellungen/{id}    → 200 OK oder 404
- GET    /{apothekenId}/bestellungen         → 200 OK (leere Liste = OK!)
- DELETE /{apothekenId}/bestellungen/{id}    → 200 OK oder 404 oder 409

### AUFGABE 3 — GlobalExceptionHandler.java
Implementiere 4 Handler:
- MethodArgumentNotValidException  → 400 (Apotheker-freundliche Nachricht!)
- BestellungNichtGefundenException → 404
- IllegalStateException            → 409 Conflict
- Exception                        → 500 (kein Stack Trace nach aussen!)

### AUFGABE 4 — Dtos.java
Fuege @Schema Annotationen zu BestellungRequestDto hinzu:
- Jedes Feld braucht description + example
- Das ist der API-Contract fuer Klaus Meier!

### AUFGABE 5 — BestellungsServiceTest.java
Schreibe 9 Unit-Tests.

---

## Nach der Implementierung testen

# Service starten:
mvn spring-boot:run

# Swagger UI oeffnen:
http://localhost:8086/swagger-ui.html

# Neue Bestellung aufgeben:
curl -X POST http://localhost:8086/api/v1/apotheke/APO-JENA-001/bestellungen \
  -H "Content-Type: application/json" \
  -d '{
    "medikament": "Carboplatin",
    "menge": 5,
    "einheit": "mg",
    "prioritaet": "DRINGEND"
  }'

# Status abfragen:
curl http://localhost:8086/api/v1/apotheke/APO-JENA-001/bestellungen/BST-2026-00123

# Alle Bestellungen:
curl http://localhost:8086/api/v1/apotheke/APO-JENA-001/bestellungen

# Stornieren:
curl -X DELETE http://localhost:8086/api/v1/apotheke/APO-JENA-001/bestellungen/BST-2026-00123

---

## REST-Design Entscheidungen — Interview-Antworten

"Warum POST → 201 und nicht 200?"
→ 200 OK = Anfrage erfolgreich verarbeitet
  201 Created = neue Ressource wurde erstellt
  Bestellung ist eine neue Ressource → 201 ist semantisch korrekt.

"Warum leere Liste bei GET alle → 200 und nicht 404?"
→ 404 bedeutet "die Ressource existiert nicht".
  Die Apotheke existiert — sie hat nur keine Bestellungen.
  Leere Liste ist ein gueltiges Ergebnis → 200 OK.

"Warum 409 Conflict beim Stornieren einer laufenden Bestellung?"
→ 409 = "Konflikt mit aktuellem Zustand der Ressource".
  Die Bestellung ist schon in Bearbeitung — das ist ein Zustandskonflikt.
  Besser als 400 (Eingabefehler) weil die Eingabe korrekt war.

"Warum Fehlermeldungen fuer den Apotheker anpassen?"
→ Apotheker Klaus ist kein IT-Experte.
  "NullPointerException in RezeptService.java:47" hilft ihm nicht.
  "Wir konnten keine Bestellung finden. Bitte pruefen Sie die ID."
  Das versteht er — und er muss nicht den IT-Support anrufen.
