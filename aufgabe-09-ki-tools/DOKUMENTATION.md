# Aufgabe 09 — KI-Tools gezielt einsetzen

## Das Konzept (fuer das Interview)

CONVALES schreibt in der Stellenanzeige explizit:
"Der gezielte Einsatz von KI-Tools zur Verbesserung Deiner Arbeitsergebnisse"

Das bedeutet: Sie suchen Entwickler die KI produktiv nutzen
aber gleichzeitig die DSGVO-Grenzen kennen.

---

## Was du mit KI machst — und was NICHT

### JA — KI einsetzen fuer:

1. Boilerplate-Code generieren
   Beispiel: "Schreib mir ein Java Record fuer diese JSON-Struktur"

2. Unit-Tests generieren
   Beispiel: "Schreib JUnit 5 Tests fuer diese Methode mit Grenzwerten"

3. Legacy-Code erklaeren
   Beispiel: "Was macht dieser XSLT-Block?"

4. Architektur-Sounding
   Beispiel: "Wie handle ich Backpressure in RabbitMQ?"

5. Fehlermeldungen debuggen
   Beispiel: anonymisierte Stack Trace einfuegen und fragen

### NEIN — KI NIEMALS fuer:

1. Echte Patientendaten einfuegen → DSGVO-Verstoss
2. Interne API-Keys / Passwoerter → Security-Verstoss
3. Firmen-internen Source Code → IP-Verlust
4. Code blind uebernehmen → immer manuell pruefen!

---

## Die Zero Data Leakage Policy

BEVOR du etwas in eine KI schickst — anonymisiere es:

```
ORIGINAL (VERBOTEN):
"Patient Max Mustermann, 65J, Carboplatin 450mg,
 Diagnose: C34.1, behandelnder Arzt: Dr. Schmidt"

ANONYMISIERT (ERLAUBT):
"Patient [ID], [Alter]J, [Medikament] [Dosierung]mg,
 Diagnose: [Code], behandelnder Arzt: [ID]"
```

---

## Spickzettel Interview-Antworten

**"Wie nutzt du KI im Programmieralltag?"**
→ Als Junior-Entwickler der mir assistiert.
  Hauptsaechlich fuer Boilerplate und Test-Generierung.
  Ich prueffe jeden KI-Code Zeile fuer Zeile bevor ich ihn committe.

**"Wo ziehst du die rote Linie?"**
→ Zero Data Leakage Policy.
  Niemals echte Patientendaten oder Firmen-Secrets in oeffentliche KI.
  Ich anonymisiere vollstaendig bevor ich Hilfe suche.

**"KI halluziniert manchmal — wie gehst du damit um?"**
→ Ich behandle KI-Code wie eine Stack-Overflow-Antwort.
  Skeptisch lesen, Zeile fuer Zeile pruefen, Tests laufen lassen.
  Unsere TeamCity-Pipeline mit SonarQube ist das finale Sicherheitsnetz.
