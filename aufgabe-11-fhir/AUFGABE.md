# Aufgabe 11 — FHIR Integration (Deutsches Profil)

## Das Szenario

CONVALES integriert sich mit dem deutschen Gesundheitsnetzwerk.
Rezepte muessen als FHIR R4 Ressourcen ausgegeben werden —
nach dem deutschen ISiK Profil (Informationstechnische Systeme im Krankenhaus).

Du bist der Java Integration Developer der:
1. FHIR Ressourcen aus internen Daten baut
2. Einen sicheren FHIR Endpunkt absichert
3. Das deutsche Profil korrekt implementiert

---

## Projektstruktur

```
aufgabe-11-fhir/
├── AUFGABE.md
├── pom.xml
└── src/
    ├── main/java/de/medipolis/fhir/
    │   ├── FhirApplication.java
    │   ├── config/
    │   │   ├── FhirConfig.java          ← HAPI FhirContext als @Bean
    │   │   └── SecurityConfig.java      ← BasicAuth Absicherung
    │   ├── controller/
    │   │   └── FhirRezeptController.java ← FHIR Endpunkte
    │   ├── mapper/
    │   │   └── RezeptFhirMapper.java    ← Dein Code → FHIR Ressource
    │   ├── model/
    │   │   └── Rezept.java              ← JPA Entity
    │   ├── repository/
    │   │   └── RezeptRepository.java
    │   └── service/
    │       └── FhirRezeptService.java   ← Geschaeftslogik
    └── test/java/de/medipolis/fhir/
        └── FhirRezeptMapperTest.java    ← DEINE AUFGABE
```

---

## Was ist FHIR?

**Fast Healthcare Interoperability Resources** — der internationale Standard
fuer den Austausch medizinischer Daten.

In Deutschland gibt es zusaetzliche Profile:
- **ISiK** — Informationstechnische Systeme im Krankenhaus
- **KBV** — Kassenaerztliche Bundesvereinigung (eRezept)
- **GEM** — Gematik (TI-Infrastruktur)

---

## FHIR Ressourcen die du kennst:

| FHIR Ressource | Bedeutung | Dein Objekt |
|---|---|---|
| `MedicationRequest` | Rezept/Verordnung | `Rezept` Entity |
| `Patient` | Patient | patientId, patientName |
| `Practitioner` | Arzt | arztName |
| `Medication` | Medikament | medikament, dosierungMg |

---

## Endpunkte

```
POST /fhir/MedicationRequest        → Rezept als FHIR speichern
GET  /fhir/MedicationRequest/{id}   → Rezept als FHIR JSON ausgeben
GET  /fhir/MedicationRequest        → Alle Rezepte als FHIR Bundle
```

Zwei Rollen:
```
fhir-reader / reader-pass-2026  → darf nur GET
fhir-writer / writer-pass-2026  → darf GET und POST
```

---

## Deutsches Profil — was bedeutet das?

Das ISiK Profil schreibt vor:
- `meta.profile` muss gesetzt sein
- `status` muss `active` sein
- `intent` muss `order` sein
- `subject` (Patient) muss eine Referenz haben
- `medication` muss ein `CodeableConcept` sein

```json
{
  "resourceType": "MedicationRequest",
  "meta": {
    "profile": ["https://gematik.de/fhir/isik/StructureDefinition/ISiKMedikationsVerordnung"]
  },
  "status": "active",
  "intent": "order",
  "subject": {
    "reference": "Patient/PAT-001",
    "display": "Max Mustermann"
  },
  "medicationCodeableConcept": {
    "text": "Carboplatin",
    "coding": [{
      "system": "http://fhir.de/CodeSystem/ifa/pzn",
      "code": "12345678",
      "display": "Carboplatin"
    }]
  },
  "dosageInstruction": [{
    "text": "450.00mg",
    "doseAndRate": [{
      "doseQuantity": {
        "value": 450.00,
        "unit": "mg",
        "system": "http://unitsofmeasure.org",
        "code": "mg"
      }
    }]
  }],
  "requester": {
    "display": "Dr. Mueller"
  }
}
```

---

## DEINE AUFGABE

### RezeptFhirMapper.java (Kern der Aufgabe!)

```
TODO 1: toMedicationRequest(Rezept rezept)
  → Rezept Entity → FHIR MedicationRequest
  → Deutsches ISiK Profil setzen (meta.profile)
  → status = active, intent = order
  → subject mit patientId und patientName
  → medicationCodeableConcept mit PZN Coding
  → dosageInstruction mit Menge und Einheit
  → requester mit arztName

TODO 2: toBundle(List<Rezept> rezepte)
  → Liste von Rezepten → FHIR Bundle
  → Bundle.type = searchset
  → Jedes Rezept als BundleEntry
```

### FhirRezeptController.java

```
TODO 3: GET /fhir/MedicationRequest/{id}
  → Rezept aus DB holen
  → Als FHIR JSON zurueckgeben (Content-Type: application/fhir+json)
  → 404 wenn nicht gefunden

TODO 4: GET /fhir/MedicationRequest
  → Alle Rezepte als FHIR Bundle zurueckgeben
```

### FhirRezeptMapperTest.java — 8 Tests implementieren

---

## Auth testen

```bash
# Mit curl — als Writer:
curl -u fhir-writer:writer-pass-2026 http://localhost:8089/fhir/MedicationRequest/1

# Als Reader:
curl -u fhir-reader:reader-pass-2026 http://localhost:8089/fhir/MedicationRequest

# Ohne Auth → 401 Unauthorized:
curl http://localhost:8089/fhir/MedicationRequest/1

# POST nur als Writer erlaubt:
curl -u fhir-writer:writer-pass-2026 -X POST \
  -H "Content-Type: application/json" \
  -d '{"patientId":"PAT-001","patientName":"Max","medikament":"Carboplatin","dosierungMg":450,"arztName":"Dr. Mueller"}' \
  http://localhost:8089/fhir/MedicationRequest
```

---

## Was der Tech-Lead fragt

"Was ist FHIR?"
→ Internationaler Standard fuer medizinische Datenaustausch.
  Krankenhaeuser, Krankenkassen, Apotheken sprechen alle FHIR.

"Was ist der Unterschied zwischen FHIR R4 und R5?"
→ R4 ist der aktuelle Standard in Deutschland (ISiK, KBV, Gematik).
  R5 ist neuer aber noch nicht weit verbreitet in DE.

"Was ist ein FHIR Bundle?"
→ Container fuer mehrere FHIR Ressourcen — wie eine Liste mit Metadaten.

"Was ist das ISiK Profil?"
→ Deutsches Profil fuer Krankenhaeuser.
  Schreibt meta.profile, Pflichtfelder und Codesysteme vor.

"Wie sicherst du einen FHIR Endpunkt ab?"
→ BasicAuth fuer einfache Faelle.
  OAuth2/SMART on FHIR fuer Production.
  In Deutschland: Gematik TI mit Konnektor und SMC-B Karte.

---

## Starten

```bash
mvn test -Dtest=FhirRezeptMapperTest
mvn spring-boot:run
```

Port: 8089
