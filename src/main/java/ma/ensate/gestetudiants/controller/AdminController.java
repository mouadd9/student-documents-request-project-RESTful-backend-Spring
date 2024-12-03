package ma.ensate.gestetudiants.controller;

import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.service.DemandeService;
import ma.ensate.gestetudiants.service.NotificationService;
import ma.ensate.gestetudiants.service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/demandes")
    public ResponseEntity<List<DemandeResponseDTO>> getAllDemandes() {
        List<DemandeResponseDTO> demandes = demandeService.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }

    @PutMapping("/demandes/{id}/approve")
    public ResponseEntity<DemandeResponseDTO> approveDemande(@PathVariable Long id) {
        DemandeResponseDTO approvedDemande = demandeService.approveDemande(id);
        notificationService.sendDemandeApprovedEmail(
                approvedDemande.getEtudiant(),
                approvedDemande
        );
        return ResponseEntity.ok(approvedDemande);
    }

    @PutMapping("/demandes/{id}/reject")
    public ResponseEntity<DemandeResponseDTO> rejectDemande(@PathVariable Long id) {
        DemandeResponseDTO rejectedDemande = demandeService.rejectDemande(id);
        notificationService.sendDemandeRejectedEmail(
                rejectedDemande.getEtudiant(),
                rejectedDemande
        );
        return ResponseEntity.ok(rejectedDemande);
    }

    @GetMapping("/reclamations")
    public ResponseEntity<List<ReclamationResponseDTO>> getAllReclamations() {
        List<ReclamationResponseDTO> reclamations = reclamationService.getAllReclamations();
        return ResponseEntity.ok(reclamations);
    }

    @PutMapping("/reclamations/{id}/treat")
    public ResponseEntity<ReclamationResponseDTO> treatReclamation(
            @PathVariable Long id,
            @Validated @RequestBody ReclamationResponseDTO reclamationResponseDTO) {
        ReclamationResponseDTO treatedReclamation = reclamationService.treatReclamation(id, reclamationResponseDTO);
        notificationService.sendReclamationTreatedEmail(
                treatedReclamation.getEtudiant(),
                treatedReclamation
        );
        return ResponseEntity.ok(treatedReclamation);
    }
}