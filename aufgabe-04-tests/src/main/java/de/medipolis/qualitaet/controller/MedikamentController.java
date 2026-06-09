package de.medipolis.qualitaet.controller;

import de.medipolis.qualitaet.model.Dtos.*;
import de.medipolis.qualitaet.service.MedikamentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medikamente")
public class MedikamentController {

    private final MedikamentService medikamentService;

    public MedikamentController(MedikamentService medikamentService) {
        this.medikamentService = medikamentService;
    }

    @PostMapping
    public ResponseEntity<MedikamentResponseDto> speichereUndPruefe(
            @Valid @RequestBody MedikamentRequestDto request) {
        return ResponseEntity.ok(medikamentService.speichereUndPruefe(request));
    }
}
