# Aufgabe 06 — Code Review

## Das Szenario

Dein Kollege Jonas hat einen neuen Rezept-Service gebaut
und einen Pull Request geoeffnet.
Du bist der Senior-Reviewer.

Der Code kompiliert und laeuft — aber er hat 10 ernste Probleme.
Deine Aufgabe: Alle finden, erklaeren, und fixen.

---

## Reihenfolge der Review

### Schritt 1: Code lesen (ohne zu runen!)
Oeffne diese Dateien in dieser Reihenfolge:
1. application.yml
2. RezeptService.java
3. RezeptController.java

### Schritt 2: Probleme dokumentieren
Fuell die Tabelle unten aus — BEVOR du fixst.

### Schritt 3: Fixen
Fixe jeden Fehler direkt im Code.

### Schritt 4: Tests schreiben
Implementiere die Tests in RezeptServiceReviewTest.java

---

## Deine Review-Liste — fuell das aus!

| # | Datei | Zeile (ca.) | Kategorie | Problem | Fix |
|---|---|---|---|---|---|
| 1 | RezeptService.java | ~32 | [blocking] SECURITY | ? | ? |
| 2 | RezeptService.java | ~42 | [blocking] DSGVO | ? | ? |
| 3 | RezeptService.java | ~44 | [blocking] MDC | ? | ? |
| 4 | RezeptService.java | ~58 | [blocking] LOGIK | ? | ? |
| 5 | RezeptService.java | ~67 | [blocking] PERFORMANCE | ? | ? |
| 6 | RezeptService.java | ~80 | [blocking] EXCEPTION | ? | ? |
| 7 | RezeptService.java | ~90 | [suggestion] ARCHITEKTUR | ? | ? |
| 8 | RezeptService.java | ~98 | [nitpick] NAMING | ? | ? |
| 9 | RezeptController.java | ~30 | [blocking] VALIDIERUNG | ? | ? |
| 10 | RezeptController.java | ~42 | [blocking] HTTP-STATUS | ? | ? |

---

## Kategorie-Labels (wie im echten Code-Review bei CONVALES)

[blocking]   = Muss gefixt werden — kein Merge ohne Fix
[suggestion] = Verbesserungsvorschlag — nicht zwingend
[nitpick]    = Kleine Sache — optional aber schoen

---

## Tests ausfuehren

mvn test -Dtest=RezeptServiceReviewTest

---

## Was der Tech-Lead im Review-Interview fragt

"Wie gehst du vor wenn du einen PR reviewst?"
→ Erst Ticket lesen (Warum wurde das gebaut?).
  Dann Tests checken (Gibt es welche? Grenzwerte?).
  Dann Code lesen: Security, DSGVO, Logik, Performance.

"Wie gehst du mit einem Kollegen um der deinen Kommentar ablehnt?"
→ Diskussion aus den Text-Kommentaren rausnehmen.
  Kurzes Teams-Gespraech oder Huddle im Buero.
  Bei kritischen Sicherheitsproblemen: Senior hinzuziehen.

"Was ist ein N+1 Query Problem?"
→ Wenn man fuer eine Liste von N Elementen
  N+1 DB-Abfragen macht statt einer.
  Fix: Eine einzige Abfrage mit IN-Clause oder JOIN.

"Warum ist Exception-Swallowing gefaehrlich?"
→ Der Aufrufer bekommt null zurueck ohne zu wissen warum.
  Fehler verschwinden im Nichts — kein Log, kein Alert.
  Im Gesundheitswesen: Rezept geht verloren, Patient bekommt
  keine Medizin, niemand weiss warum.
