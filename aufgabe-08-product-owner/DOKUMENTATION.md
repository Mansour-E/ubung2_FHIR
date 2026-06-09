# Aufgabe 08 — Gemeinsame Konzeptionierung mit dem Product Owner

## Was ist ein Product Owner (PO)?

Der PO bei CONVALES/Medipolis:
- Weiss WAS gebaut werden soll (Business-Anforderungen)
- Weiss NICHT wie es technisch umgesetzt wird
- Verwaltet den Backlog (Liste aller Aufgaben)
- Spricht mit Krankenhaeusern, Apotheken, Krankenkassen

Du als Entwickler:
- Weisst WIE es gebaut werden soll
- Sagst dem PO was technisch moeglich ist
- Uebersetzt Business-Anforderungen in Tickets
- Bist kein Befehlsempfaenger — du bist Partner auf Augenhoehe

---

## Das typische Meeting: Refinement Session

```
PO: "Die Krankenkasse AOK will ihre Patientendaten
     live alle 5 Minuten mit uns synchronisieren."

Du (denkst):
  Polling alle 5 Minuten auf unsere DB = Performance-Problem
  Bei 100 Krankenkassen = 100 x 5 Min Polling = Server-Kollaps

Du (sagst):
  "Ich verstehe das Ziel. Aber 5-Minuten-Polling wuerde
   unsere Datenbank unter hoher Last zum Absturz bringen.
   Ich schlage eine Event-driven Architektur vor:
   Wir schicken der AOK einen Webhook wenn sich wirklich
   etwas aendert. Dann bekommt sie die Daten sofort —
   und unser Server bleibt stabil."

PO: "Was brauchen wir dafuer?"

Du: "Zwei Wochen Entwicklung. Ich schreibe heute noch
     das technische Konzept."
```

---

## Die 4 wichtigsten Situationen im Interview

---

### SITUATION 1: Enge Deadline — zu wenig Zeit

PO sagt:
"Wir brauchen die komplette HL7-Integration mit allen
 Krankenhaeusern in Thueringen bis naechsten Monat."

Falsche Antwort:
"Das geht nicht."

Richtige Antwort — MVP vorschlagen:
"Wir koennen in einem Monat nicht alle 47 Krankenhaeuser
 anbinden. Aber wir koennen folgendes liefern:
 Phase 1 (1 Monat): Ein Pilot-Krankenhaus, nur Entlassmeldungen,
                    HL7-Format, vollstaendig getestet.
 Phase 2 (2 Monate): Alle weiteren Krankenhaeuser replizieren.

 Das Pilot-Krankenhaus gibt uns echtes Feedback und reduziert
 das Risiko. Sind Sie damit einverstanden?"

Schluesselwort: MVP (Minimum Viable Product)
Prinzip: Iron Triangle — Scope, Zeit, Qualitaet.
         Qualitaet ist nicht verhandelbar. Zeit ist fix.
         Also reduzieren wir den Scope.

---

### SITUATION 2: Vage Anforderung aufschlusseln

PO sagt:
"Wir muessen uns mit allen Partnern im deutschen
 Gesundheitssystem anbinden."

Das ist zu vage. Du hilfst aufzuschluesseln:

Schritt 1 — Fragen stellen:
  "Welche Partner genau? Krankenhaeuser, Arztpraxen oder Krankenkassen?"
  "Welche Datenpakete sollen uebertragen werden?"
  "Welches Format nutzen die Partner? HL7, FHIR, EDIFACT?"
  "Was ist die hoehere Prioritaet fuer den Business-Wert?"

Schritt 2 — Vertikal schneiden (INVEST Prinzip):
  NICHT: "Alle Partner anbinden" (viel zu gross)
  SONDERN:
    Story 1: Entlassmeldungen von Krankenhaus Jena empfangen (HL7)
    Story 2: Abrechnungen an Krankenkasse AOK senden (EDIFACT)
    Story 3: Rezepte von Arztpraxen empfangen (FHIR)

Schritt 3 — Story Points schaetzen (Planning Poker):
  Story 1: 5 Punkte (1 Sprint)
  Story 2: 8 Punkte (2 Sprints, komplex durch EDIFACT)
  Story 3: 13 Punkte (3 Sprints, neues FHIR Format)

---

### SITUATION 3: Technical Debt erkaeren

PO sagt:
"Wir muessen in diesem Sprint nur neue Features bauen.
 Keine Zeit fuer technische Wartung."

Falsche Antwort:
"Unser Code ist unordentlich und muss aufgeraeumt werden."

Richtige Antwort — Risiken in Business-Sprache:
"Ich verstehe den Druck. Aber ich muss Sie auf ein Risiko
 hinweisen:

 Unser Orchestra-Server braucht ein Sicherheitsupdate.
 Ohne das Update:
 - Risiko 1: Sicherheitszertifizierung laeuft ab naechsten Monat ab
 - Risiko 2: Das naechste Feature dauert 50% laenger weil wir
             auf veralteter Infrastruktur bauen
 - Risiko 3: Bei einem Ausfall: 4h Downtime statt 30min

 Ich schlage vor: 15% des Sprints (ca. 1 Tag) fuer das Update.
 Das schuetzt uns vor allen drei Risiken."

Schluessel: Technische Schulden = Geschaeftsrisiken uebersetzen

---

### SITUATION 4: Unsicherer Workaround vom Anwender

PO sagt:
"Der Apotheker will Patientendaten als Excel-Export
 per E-Mail bekommen."

Falsche Antwort:
"Das geht nicht — DSGVO."

Richtige Antwort — Job-to-be-Done verstehen:
"Ich verstehe das Ziel des Apothekers: Er braucht
 schnellen Zugriff auf bestimmte Patientendaten.

 Aber ein unverschluesselter Excel-Export per E-Mail
 waere ein kritischer DSGVO-Verstoss gemaess Art. 32.
 Das koennte zu Bussgeldern bis 20 Mio Euro fuehren.

 Alternativvorschlag:
 Wir bauen ein sicheres, rollenbasiertes Dashboard
 direkt in unserer Webanwendung. Der Apotheker kann
 seine Daten dort filtern und exportieren — aber nur
 verschluesselt und mit Audit-Log.

 Das loest sein eigentliches Problem sicher und DSGVO-konform."

---

## Gherkin Syntax — Akzeptanzkriterien schreiben

Gherkin verbindet Business-Anforderungen mit automatisierten Tests.
PO und Entwickler sprechen dieselbe Sprache.

Beispiel — Story: "Rezept von Krankenhaus empfangen"

```
Feature: Rezept-Empfang vom Krankenhaus Jena

  Scenario: Gueltiges Rezept wird erfolgreich verarbeitet
    Given das Krankenhaus Jena sendet ein gueltiges HL7-Rezept
    And das Rezept enthaelt: PatientId, Medikament, Dosierung
    When der Integrationsservice das Rezept empfaengt
    Then wird das Rezept in der Datenbank gespeichert
    And die Apotheke bekommt den Produktionsauftrag
    And das Krankenhaus erhaelt eine Bestaetigung (202 Accepted)

  Scenario: Rezept mit fehlender Dosierung wird abgelehnt
    Given das Krankenhaus sendet ein Rezept ohne Dosierung
    When der Integrationsservice das Rezept empfaengt
    Then wird das Rezept NICHT gespeichert
    And das Krankenhaus erhaelt eine Fehlermeldung (400 Bad Request)
    And ein Ticket wird fuer den 2nd-Level-Support erstellt
```

Warum ist das nuetzlich?
- PO: Versteht was gebaut wird (kein Code noetig)
- Entwickler: Weiss genau was der Test pruefen muss
- QA: Kann den Test automatisieren (z.B. mit Cucumber)

---

## Die wichtigsten Tools im PO-Gespraech

| Tool | Wofuer |
|---|---|
| Jira | User Stories, Epics, Sprint-Planung |
| Confluence | Technische Dokumentation, Entscheidungen |
| Miro / Draw.io | Sequenzdiagramme, Datenfluss-Charts |
| OpenAPI/Swagger | API-Contract vor der Implementierung |
| Gherkin | Akzeptanzkriterien die testbar sind |
| Planning Poker | Story Points schaetzen |

---

## Interview-Fragen und Antworten

---

**"Wie gehst du mit einem PO um der eine unmoeglich enge Deadline setzt?"**

Antwort:
"Ich sage nicht einfach Nein. Ich nutze das Iron Triangle:
 Scope, Zeit und Qualitaet. Da Qualitaet im E-Health-Bereich
 nicht verhandelbar ist und die Zeit vom PO festgelegt wird,
 spreche ich ueber den Scope. Ich schlage ein MVP vor:
 Was ist das Minimum das wir in der Zeit liefern koennen
 das echten Business-Wert hat? Dann planen wir Phase 2."

---

**"Wie hilfst du dem PO eine vage Anforderung aufzuschluesseln?"**

Antwort:
"Ich nutze das INVEST-Prinzip: jede User Story soll
 Independent, Negotiable, Valuable, Estimable, Small
 und Testable sein. Bei einer grossen Anforderung
 schneide ich sie vertikal — nicht nach technischen
 Schichten, sondern nach Business-Funktionalitaet.
 Wir fangen mit dem duennsten moeglichen Slice an
 der echter Wert liefert, testen ihn, und replizieren."

---

**"Wie erklaerst du Technical Debt einem PO der nur neue Features will?"**

Antwort:
"Ich uebersetze technische Schulden in Geschaeftsrisiken.
 Nicht: 'Der Code ist haeßlich.'
 Sondern: 'Wenn wir das nicht in diesem Sprint fixen,
           kostet das naechste Feature doppelt so viel Zeit
           und unser Sicherheitszertifikat laeuft ab.'
 Ein guter PO versteht sofort die Risikoabwaegung.
 Ich schlage immer ein fixes Budget von 15-20% pro Sprint
 fuer technische Wartung vor."

---

**"Was ist der Unterschied zwischen einem Epic und einer User Story?"**

Antwort:
"Ein Epic ist eine grosse, uebergeordnete Anforderung
 die noch zu gross fuer einen Sprint ist.
 Beispiel Epic: 'Alle Krankenhaeuser in Thueringen anbinden'

 Eine User Story ist ein konkreter, kleiner Slice
 der in einem Sprint umgesetzt werden kann.
 Beispiel Story: 'Als Arzt im Uni-Klinikum Jena
                  moechte ich Entlassmeldungen digital senden
                  damit die Apotheke sofort informiert wird.'

 Die Story hat Akzeptanzkriterien in Gherkin-Syntax
 und einen klaren Definition of Done."
