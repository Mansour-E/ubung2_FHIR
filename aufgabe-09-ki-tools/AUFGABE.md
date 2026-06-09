# Aufgabe 09 — KI-Tools gezielt einsetzen

## Das Szenario

Orchestra empfaengt einen Arztbrief von einem Krankenhaus.
Orchestra kann keinen Freitext analysieren — zu komplex.
Also schickt Orchestra den Brief an diesen KI-Microservice.

```
Orchestra
    │ POST /api/v1/ki/arztbrief/analysiere
    │ Body: { arztbriefText: "...", quellSystem: "Orchestra" }
    ▼
KiController
    │
    ▼
KiAnalyseService
    │
    ├── SCHRITT 1: AnonymisierungsService
    │   Patientendaten ersetzen BEVOR KI-Call!
    │   (DSGVO Zero Data Leakage Policy)
    │
    ├── SCHRITT 2: KiApiClient
    │   Anonymisierten Text zur KI schicken
    │   KI extrahiert: Medikament, Dosierung, Diagnose
    │
    └── SCHRITT 3: JSON parsen + DTO bauen
        Zurueck an Orchestra
    ▼
HTTP 200 OK
Body: ExtrahierteMedikamentendatenDto
```

---

## Deine Aufgaben

### AUFGABE 1 — AnonymisierungsService.java
Implementiere 4 Methoden:
- anonymisiere()    → Hauptmethode: alle Schritte zusammen
- ersetzeNamen()    → Patient/Dr./Arzt anonymisieren
- ersetzeDaten()    → Datumsformate anonymisieren
- ersetzeIds()      → PAT-IDs und Versicherungsnummern

### AUFGABE 2 — KiAnalyseService.java
Implementiere 2 Methoden:
- analysiereArztbrief() → anonymisieren → KI aufrufen → parsen
- parseKiAntwort()      → KI-JSON zu DTO umwandeln (try-catch!)

### AUFGABE 3 — KiController.java
Implementiere 1 Endpunkt:
- analysiereArztbrief() → MDC, Logging, Service aufrufen, 200 OK

### AUFGABE 4 — AnonymisierungsServiceTest.java
Schreibe 9 Unit-Tests fuer die Anonymisierung.
Das ist der DSGVO-Beweis!

### AUFGABE 5 — KiAnalyseServiceTest.java
Schreibe 6 Unit-Tests mit Mockito.
Besonders wichtig: Beweise dass anonymisiere() VOR dem KI-Call aufgerufen wird!

---

## Tests ausfuehren

mvn test -Dtest=AnonymisierungsServiceTest
mvn test -Dtest=KiAnalyseServiceTest
mvn test

---

## Was der Tech-Lead fragt

"Warum anonymisierst du VOR dem KI-Call?"
→ DSGVO Art. 9 — Gesundheitsdaten sind besondere Kategorie.
  Echte Patientendaten in oeffentliche KI = sofortiger
  Compliance-Verstoss. Anonymisierung ist Pflicht.

"Wie beweist du im Test dass die Reihenfolge stimmt?"
→ Mit InOrder von Mockito:
  inOrder.verify(anonymisierungsService).anonymisiere(any());
  inOrder.verify(kiApiClient).analysiereText(any());

"Was machst du wenn die KI halluziniert?"
→ parseKiAntwort() ist in try-catch.
  Ungueltige Antwort → status="KI_FEHLER" → Alert → Mensch prueft.
  Im Gesundheitswesen niemals blind der KI vertrauen.

"Wie sicherst du den KI-API-Key?"
→ ${KI_API_KEY} aus Umgebungsvariable.
  In TeamCity: masked parameter.
  In Produktion: HashiCorp Vault.
  Niemals committed.
