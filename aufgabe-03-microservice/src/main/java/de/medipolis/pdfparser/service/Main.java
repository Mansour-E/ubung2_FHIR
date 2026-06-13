package de.medipolis.pdfparser.service;

import de.medipolis.pdfparser.model.Dtos;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        PdfParserService pdfParserService = new PdfParserService();
        Dtos.PdfParseRequestDto request = new Dtos.PdfParseRequestDto("PATIENT_ID:;MEDIKAMENT:Carboplatin;DOSIERUNG:450.5;EINHEIT:mg",
                "klkrankenhaus");
        pdfParserService.parsePdf(request, UUID.randomUUID().toString());

        System.out.println(pdfParserService.parsePdf(request, UUID.randomUUID().toString()));
    }
}
