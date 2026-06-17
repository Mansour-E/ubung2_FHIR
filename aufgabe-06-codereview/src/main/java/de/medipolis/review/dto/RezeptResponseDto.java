package de.medipolis.review.dto;

import de.medipolis.review.model.Rezept;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Reference;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RezeptResponseDto {

    private Long id;
    private String patientId;
    private String patientName;
    private String medikament;
    private BigDecimal dosierungMg;
    private String arztName;
    private String status;







    public static RezeptResponseDto fromRezept(Rezept rezept) {

        RezeptResponseDto responseDto = new RezeptResponseDto();

        responseDto.id = rezept.getId();
        responseDto.patientId =  rezept.getPatientId();
        responseDto.patientName = rezept.getPatientName();
        responseDto.medikament = rezept.getMedikament();
        responseDto.dosierungMg = rezept.getDosierungMg();
        responseDto.arztName = rezept.getArztName();
        responseDto.status = rezept.getStatus();

        return responseDto;
    }

    public String formatiert() {
        return  "Patient: " + patientName +
                    " | Medikament: " + medikament +
                    " | Dosierung: " + dosierungMg + "mg" +
                    " | Status: " + status;

    }

    public MedicationRequest toFhir(){

        MedicationRequest fhir = new MedicationRequest();

        fhir.setSubject(new Reference("Patient/"+patientId));

        fhir.setMedication(new CodeableConcept().setText(medikament));

        Dosage dosage = new Dosage();
        dosage.setText(dosierungMg+"mg");
        fhir.addDosageInstruction(dosage);

        fhir.setRequester(new Reference("Practitioner/"+arztName));

        return fhir;

    }
}
