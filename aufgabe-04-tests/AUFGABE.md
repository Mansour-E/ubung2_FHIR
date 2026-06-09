# Aufgabe 04 — Code-Qualitaet durch Unit- und Integrationstests

## Das Szenario

Du testest einen MedikamentService der:
1. Medikamente in PostgreSQL speichert
2. Die externe Krankenkasse um Genehmigung fragt
3. Das Ergebnis zurueckgibt

Der Produktionscode (MedikamentService) ist bereits fertig.
DEINE AUFGABE: Schreibe die Tests!

---

## Der Unterschied — Unit vs Integration

```
UNIT-TEST (MedikamentServiceUnitTest)
──────────────────────────────────────
✅ Kein Spring Context
✅ Kein Docker, keine echte DB
✅ Mockito mockt Repository und KrankenkasseClient
✅ Laueft in Millisekunden
✅ Testet reine Business-Logik isoliert

Starten: mvn test -Dtest=MedikamentServiceUnitTest


INTEGRATIONSTEST (MedikamentIntegrationTest)
──────────────────────────────────────
✅ Echter Spring Context (@SpringBootTest)
✅ Echter PostgreSQL Container (Testcontainers — kein H2!)
✅ WireMock simuliert externe Krankenkasse
✅ MockMvc schickt echte HTTP Requests
✅ Testet das Zusammenspiel aller Komponenten

Starten: mvn test -Dtest=MedikamentIntegrationTest
(Docker muss laufen!)
```

---

## Deine Aufgaben

### AUFGABE 1 — MedikamentServiceUnitTest.java
Unit-Tests mit Mockito schreiben:

**1A: speichereUndPruefe() testen (5 Tests)**
- Krankenkasse gibt GENEHMIGT → korrekt im Response
- Krankenkasse gibt ABGELEHNT → korrekt im Response
- Krankenkasse nicht erreichbar → UNBEKANNT
- Dosierung 0 → IllegalArgumentException
- repository.save() wird genau 1x aufgerufen (verify)

**1B: berechneGewichtsDosis() testen (4 Tests)**
- Normale Berechnung funktioniert
- Basisdosis 0 → IllegalArgumentException
- Gewicht negativ → IllegalArgumentException
- Basisdosis null → IllegalArgumentException

### AUFGABE 2 — MedikamentIntegrationTest.java
Integrationstests mit Testcontainers + WireMock schreiben:

**2A: Happy Path (2 Tests)**
- Gueltiges Medikament → 200 OK, in DB gespeichert
- Fehlende Felder → 400 Bad Request

**2B: Fehlerszenarien mit WireMock (3 Tests)**
- Krankenkasse gibt 500 → Service gibt UNBEKANNT
- Krankenkasse hat Timeout → Service gibt UNBEKANNT
- Krankenkasse gibt ABGELEHNT → korrekt im Response

**2C: Datenbank-Persistenz (1 Test)**
- Zwei Medikamente werden korrekt gespeichert

---

## Mockito Spickzettel

```java
// Mock erstellen:
@Mock
private MedikamentRepository repository;

// Verhalten definieren:
when(repository.save(any())).thenReturn(gespeichertesObjekt);

// Pruefen ob aufgerufen wurde:
verify(repository, times(1)).save(any());
verify(repository, never()).save(any());  // wurde NIE aufgerufen

// Exception simulieren:
when(krankenkasseClient.pruefeGenehmigung(any(), any()))
    .thenThrow(new RuntimeException("Netzwerkfehler"));
```

## WireMock Spickzettel

```java
// 200 OK simulieren:
wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
    .willReturn(WireMock.aResponse()
        .withStatus(200)
        .withBody("GENEHMIGT")));

// 500 Fehler simulieren:
wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
    .willReturn(WireMock.aResponse()
        .withStatus(500)));

// Timeout simulieren:
wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/genehmigung"))
    .willReturn(WireMock.aResponse()
        .withFixedDelay(3000)  // 3 Sekunden
        .withStatus(200)));
```

---

## Was der Tech-Lead fragt

"Warum Testcontainers und nicht H2?"
→ H2 bildet PostgreSQL-Dialekte nicht korrekt ab.
  Mit Testcontainers teste ich gegen echte PostgreSQL.
  Was im Test funktioniert, funktioniert auch in Produktion.

"Was ist der Unterschied zwischen Unit und Integrationstest?"
→ Unit: isoliert, mockt alles externe, blitzschnell.
  Integration: testet Zusammenspiel, echter Stack.
  Beides brauchen wir — Unit fuer Logik, Integration fuer Grenzen.

"Was testest du mit WireMock?"
→ Nicht nur den Erfolgsweg (200 OK).
  Ich simuliere bewusst 500 Fehler, Timeouts, 401 Unauthorized.
  Dann pruefen ich ob mein Service das sauber abfaengt
  ohne selbst abzustuerzen.

"Warum verify() in Unit-Tests?"
→ Es reicht nicht zu pruefen ob das Ergebnis stimmt.
  Ich muss auch pruefen ob der richtige Aufruf stattgefunden hat.
  Z.B. dass save() genau einmal und nicht zweimal aufgerufen wurde.
