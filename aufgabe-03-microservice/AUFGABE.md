# Aufgabe 03 — Entwicklung von Microservices in Java

## Das Szenario (wie im echten Job bei CONVALES)

Orchestra empfaengt ein PDF-Rezept von einem Arzt.
Orchestra kann kein PDF lesen — zu komplex.
Also schickt Orchestra den PDF-Inhalt an diesen eigenstaendigen
Java-Microservice der genau EINE Aufgabe hat: PDF parsen.

```
Orchestra
    │ POST /api/v1/pdf-parser/parse
    │ Body: { pdfInhalt: "...", quellSystem: "Orchestra" }
    ▼
PdfParserController      (empfaengt Request)
    │
    ▼
PdfParserService         (extrahiert + validiert Felder)
    │
    ▼
HTTP 200 OK              (JSON zurueck an Orchestra)
    Body: ParseErgebnisDto
```

Das PDF-Format (vereinfacht):
  "PATIENT_ID:PAT-001;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg"

---

## Warum ist das ein Microservice?

- Eigene pom.xml (unabhaengiges Projekt)
- Eigener Port (8083)
- Eigene einzige Aufgabe: PDF parsen
- Virtual Threads aktiviert (Java 21) → skaliert automatisch
- Wenn dieser Service abstuerzt → Orchestra laeuft weiter

Das ist genau was der Tech-Lead hoeren will wenn er fragt:
"Was ist der Vorteil eines eigenstaendigen Microservice?"

---

## Deine Aufgaben

### AUFGABE 1 — PdfParserService.java
Implementiere:
- parsePdf()           → Hauptmethode: extrahieren + validieren + DTO bauen
- extrahiereFelder()   → "KEY:WERT;KEY:WERT" → Map<String, String>
- validiereFelder()    → alle 4 Pflichtfelder pruefen

### AUFGABE 2 — PdfParserController.java
Implementiere:
- parsePdf()           → Request empfangen, Service aufrufen, 200 OK
- MDC mit try-finally
- DSGVO: keine Patientendaten ins Log!

### AUFGABE 3 — GlobalExceptionHandler.java
Implementiere:
- MethodArgumentNotValidException → 400
- PdfParseException               → 400
- Exception (catch-all)           → 500 (kein Stack Trace nach aussen!)

### AUFGABE 4 — PdfParserServiceTest.java
Schreibe alle 8 Unit-Tests:
- 3 Tests fuer extrahiereFelder()
- 5 Tests fuer parsePdf()

---

## Tests ausfuehren

# Unit-Tests (kein Docker noetig):
mvn test -Dtest=PdfParserServiceTest

# Alle Tests:
mvn test

---

## Manuell testen (nach Implementierung)

# Service starten:
mvn spring-boot:run

# Gueltiger Request:
curl -X POST http://localhost:8083/api/v1/pdf-parser/parse \
  -H "Content-Type: application/json" \
  -d '{
    "pdfInhalt": "PATIENT_ID:PAT-001;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg",
    "quellSystem": "Orchestra"
  }'

# Erwartete Antwort (200 OK):
{
  "correlationId": "...",
  "patientId": "PAT-001",
  "medikament": "Carboplatin",
  "dosierungMg": 450.5,
  "einheit": "mg",
  "status": "ERFOLGREICH"
}

# Fehlerhafter Request (leerer pdfInhalt):
curl -X POST http://localhost:8083/api/v1/pdf-parser/parse \
  -H "Content-Type: application/json" \
  -d '{
    "pdfInhalt": "",
    "quellSystem": "Orchestra"
  }'

# Erwartete Antwort (400 Bad Request):
{
  "correlationId": null,
  "fehlerCode": "VALIDIERUNG_FEHLER",
  "nachricht": "pdfInhalt: PDF-Inhalt darf nicht leer sein"
}

---

## Was der Tech-Lead fragt

"Warum 200 OK hier aber 202 Accepted im ersten Projekt?"
→ Dieser Service antwortet synchron. Orchestra wartet auf das Ergebnis.
  200 = fertig. 202 = wird noch verarbeitet (asynchron).

"Warum Virtual Threads in application.yml?"
→ Bei 500 parallelen Anfragen wuerden Platform-Threads
  den Speicher aufbrauchen. Virtual Threads (Java 21)
  skalieren automatisch mit minimalem Speicherverbrauch.

"Warum extrahiereFelder() package-private statt private?"
→ Direkte Testbarkeit ohne HTTP-Schicht.
  Unit-Tests koennen die Methode isoliert testen.

"Was wuerdest du in Produktion statt dem Text-Format nutzen?"
→ Apache PDFBox fuer echte PDFs, oder einen KI-Service.
  Das Muster bleibt gleich: extrahieren, validieren, transformieren.
