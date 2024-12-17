package ma.ensate.gestetudiants.controller;

import ma.ensate.gestetudiants.dto.demande.DemandeRequestDTO;
import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationRequestDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.dto.statistiques.StatistiquesDTO;
import ma.ensate.gestetudiants.enums.TypeDocument;
import ma.ensate.gestetudiants.service.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DemandeService demandeService;
    private final ReclamationService reclamationService;
    private final DocumentGenerationService documentGenerationService;
    private final StatistiquesService statistiquesService;

    @GetMapping("/demandes")
    public ResponseEntity<List<DemandeResponseDTO>> getAllDemandes() {
        return ResponseEntity.ok(demandeService.getAllDemandes());
    }

    @GetMapping("/demandes/{id}")
    public ResponseEntity<DemandeResponseDTO> getDemandeById(@PathVariable Long id) {
        return ResponseEntity.ok(demandeService.getDemandeById(id));
    }

    @PostMapping("/demandes")
    public ResponseEntity<DemandeResponseDTO> createDemande(@Validated @RequestBody DemandeRequestDTO demandeDTO) {
        DemandeResponseDTO demandeResponse = demandeService.createDemande(demandeDTO);
        return new ResponseEntity<>(demandeResponse, HttpStatus.CREATED);
    }

    @PutMapping("/demandes/{id}/approve")
    public ResponseEntity<DemandeResponseDTO> approveDemande(@PathVariable Long id) {
        DemandeResponseDTO approvedDemande = demandeService.approveDemande(id);
        return ResponseEntity.ok(approvedDemande);
    }

    @PutMapping("/demandes/{id}/reject")
    public ResponseEntity<DemandeResponseDTO> rejectDemande(@PathVariable Long id) {
        DemandeResponseDTO rejectedDemande = demandeService.rejectDemande(id);
        return ResponseEntity.ok(rejectedDemande);
    }

    @GetMapping("/reclamations")
    public ResponseEntity<List<ReclamationResponseDTO>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    @GetMapping("/reclamations/{id}")
    public ResponseEntity<ReclamationResponseDTO> getReclamationById(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.getReclamationById(id));
    }

    @PostMapping("/reclamations")
    public ResponseEntity<ReclamationResponseDTO> createReclamation(
            @Validated @RequestBody ReclamationRequestDTO reclamationDTO) {
        ReclamationResponseDTO reclamationResponse = reclamationService.createReclamation(reclamationDTO);
        return new ResponseEntity<>(reclamationResponse, HttpStatus.CREATED);
    }

    @PutMapping("/reclamations/{id}/treat")
    public ResponseEntity<ReclamationResponseDTO> treatReclamation(
            @PathVariable Long id,
            @Validated @RequestBody ReclamationResponseDTO reclamationResponseDTO) {
        ReclamationResponseDTO treatedReclamation = reclamationService.treatReclamation(id, reclamationResponseDTO);
        return ResponseEntity.ok(treatedReclamation);
    }

    @GetMapping("/statistiques")
    public ResponseEntity<StatistiquesDTO> getStatistiques() {
        return ResponseEntity.ok(statistiquesService.getStatistiques());
    }

    @GetMapping("/demandes/{id}/pdf")
    public ResponseEntity<byte[]> getDemandePdf(@PathVariable Long id) throws ExecutionException, InterruptedException {
        DemandeResponseDTO demande = demandeService.getDemandeById(id);
        byte[] pdfBytes = documentGenerationService
                .generateDocument(demande.getTypeDocument(), demande.getEtudiant().getId());
        return ResponseEntity.ok()
                .headers(createPdfHeaders(demande))
                .body(pdfBytes);
    }

    // Private helper methods
    private HttpHeaders createPdfHeaders(DemandeResponseDTO demande) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = demande.getTypeDocument() == TypeDocument.ATTESTATION_SCOLARITE
                ? "Attestation_Scolarite_" + demande.getEtudiant().getNom() + ".pdf"
                : "Releve_de_Notes_" + demande.getEtudiant().getNom() + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);
        return headers;
    }
}