package de.medipolis.fhir.mapper;

import de.medipolis.fhir.model.Rezept;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ============================================================
 * DEINE HAUPTAUFGABE — RezeptFhirMapper
 * ============================================================
 *
 * Dieser Mapper konvertiert interne Rezept-Entities zu FHIR R4 Ressourcen.
 * Er implementiert das deutsche ISiK Profil.
 *
 * ISiK = Informationstechnische Systeme im Krankenhaus
 * Profil: https://gematik.de/fhir/isik/StructureDefinition/ISiKMedikationsVerordnung
 *
 * FHIR Klassen die du brauchst:
 *   MedicationRequest  → das Rezept
 *   Bundle             → Liste von Rezepten
 *   Reference          → Verweis auf Patient oder Arzt
 *   CodeableConcept    → Medikament mit Code
 *   Coding             → PZN Code
 *   Dosage             → Dosierungsanweisung
 *   Quantity           → Menge mit Einheit
 */
@Component
public class RezeptFhirMapper {

    // Deutsches ISiK Profil URL
    private static final String ISIK_PROFILE =
        "https://gematik.de/fhir/isik/StructureDefinition/ISiKMedikationsVerordnung";

    // PZN Code System — Pharmazentralnummer (deutscher Standard)
    private static final String PZN_SYSTEM = "http://fhir.de/CodeSystem/ifa/pzn";

    // UCUM Einheitensystem (international standard fuer Mengenangaben)
    private static final String UCUM_SYSTEM = "http://unitsofmeasure.org";

    /**
     * TODO 1 — Implementiere diese Methode!
     *
     * Konvertiert eine Rezept Entity zu einem FHIR MedicationRequest.
     * Muss das deutsche ISiK Profil implementieren.
     *
     * Schritt fuer Schritt:
     *
     * SCHRITT 1: MedicationRequest erstellen
     *   MedicationRequest mr = new MedicationRequest();
     *
     * SCHRITT 2: ID setzen
     *   mr.setId(rezept.getId().toString());
     *
     * SCHRITT 3: Deutsches Profil setzen
     *   mr.getMeta().addProfile(ISIK_PROFILE);
     *
     * SCHRITT 4: Status — ISiK schreibt "active" vor
     *   mr.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
     *
     * SCHRITT 5: Intent — ISiK schreibt "order" vor
     *   mr.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
     *
     * SCHRITT 6: Patient setzen
     *   Reference subject = new Reference();
     *   subject.setReference("Patient/" + rezept.getPatientId());
     *   subject.setDisplay(rezept.getPatientName());
     *   mr.setSubject(subject);
     *
     * SCHRITT 7: Medikament mit PZN setzen
     *   CodeableConcept medication = new CodeableConcept();
     *   medication.setText(rezept.getMedikament());
     *   if (rezept.getPzn() != null) {
     *       Coding coding = new Coding();
     *       coding.setSystem(PZN_SYSTEM);
     *       coding.setCode(rezept.getPzn());
     *       coding.setDisplay(rezept.getMedikament());
     *       medication.addCoding(coding);
     *   }
     *   mr.setMedication(medication);
     *
     * SCHRITT 8: Dosierung setzen
     *   Dosage dosage = new Dosage();
     *   dosage.setText(rezept.getDosierungMg() + rezept.getEinheit());
     *   Dosage.DosageDoseAndRateComponent doseRate = new Dosage.DosageDoseAndRateComponent();
     *   Quantity quantity = new Quantity();
     *   quantity.setValue(rezept.getDosierungMg());
     *   quantity.setUnit(rezept.getEinheit());
     *   quantity.setSystem(UCUM_SYSTEM);
     *   quantity.setCode(rezept.getEinheit());
     *   doseRate.setDose(quantity);
     *   dosage.addDoseAndRate(doseRate);
     *   mr.addDosageInstruction(dosage);
     *
     * SCHRITT 9: Arzt setzen
     *   Reference requester = new Reference();
     *   requester.setDisplay(rezept.getArztName());
     *   mr.setRequester(requester);
     *
     * SCHRITT 10: return mr;
     */
    public MedicationRequest toMedicationRequest(Rezept rezept) {
        // TODO 1: Implementiere hier!
        throw new UnsupportedOperationException("TODO 1: toMedicationRequest() implementieren!");
    }

    /**
     * TODO 2 — Implementiere diese Methode!
     *
     * Konvertiert eine Liste von Rezepten zu einem FHIR Bundle.
     * Bundle = Container fuer mehrere FHIR Ressourcen.
     *
     * SCHRITT 1: Bundle erstellen
     *   Bundle bundle = new Bundle();
     *
     * SCHRITT 2: Typ setzen — searchset fuer Suchergebnisse
     *   bundle.setType(Bundle.BundleType.SEARCHSET);
     *
     * SCHRITT 3: Anzahl setzen
     *   bundle.setTotal(rezepte.size());
     *
     * SCHRITT 4: Jeden Rezept als Entry hinzufuegen
     *   for (Rezept rezept : rezepte) {
     *       Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
     *       entry.setResource(toMedicationRequest(rezept));
     *       bundle.addEntry(entry);
     *   }
     *
     * SCHRITT 5: return bundle;
     */
    public Bundle toBundle(List<Rezept> rezepte) {
        // TODO 2: Implementiere hier!
        throw new UnsupportedOperationException("TODO 2: toBundle() implementieren!");
    }
}
