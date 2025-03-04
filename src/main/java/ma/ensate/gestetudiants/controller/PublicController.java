package ma.ensate.gestetudiants.controller;

import ma.ensate.gestetudiants.dto.demande.DemandeRequestDTO;
import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationRequestDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.service.DemandeService;
import ma.ensate.gestetudiants.service.ReclamationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final DemandeService demandeService;
    private final ReclamationService reclamationService;

    @PostMapping("/demandes")
    public ResponseEntity<DemandeResponseDTO> createDemande(@Validated @RequestBody DemandeRequestDTO demandeDTO) {
        DemandeResponseDTO demandeResponse = demandeService.createDemande(demandeDTO);
        return new ResponseEntity<>(demandeResponse, HttpStatus.CREATED);
    }

    @PostMapping("/reclamations")
    public ResponseEntity<ReclamationResponseDTO> createReclamation(@Validated @RequestBody ReclamationRequestDTO reclamationDTO) {
        ReclamationResponseDTO reclamationResponse = reclamationService.createReclamation(reclamationDTO);
        return new ResponseEntity<>(reclamationResponse, HttpStatus.CREATED);
    }
}