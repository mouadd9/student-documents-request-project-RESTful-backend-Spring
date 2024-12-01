package ma.ensate.gestetudiants.controller;

import ma.ensate.gestetudiants.dto.ReclamationTreatmentDTO;
import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Reclamation;
import ma.ensate.gestetudiants.service.DemandeService;
import ma.ensate.gestetudiants.service.NotificationService;
import ma.ensate.gestetudiants.service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Demande>> getAlldemandes() {
        List<Demande> demandes = demandeService.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }

    @PutMapping("/demandes/{id}/approve")
    public ResponseEntity<Demande> approveDemande(@PathVariable Long id) {
        Demande approvedDemande = demandeService.approveDemande(id);
        notificationService.sendDemandeApprovedEmail(approvedDemande.getEtudiant(), approvedDemande);
        return ResponseEntity.ok(approvedDemande);
    }

    @PutMapping("/demandes/{id}/reject")
    public ResponseEntity<Demande> rejectDemande(@PathVariable Long id) {
        Demande rejectedDemande = demandeService.rejectDemande(id);
        notificationService.sendDemandeRejectedEmail(rejectedDemande.getEtudiant(), rejectedDemande);
        return ResponseEntity.ok(rejectedDemande);
    }

    @GetMapping("/reclamations")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        List<Reclamation> reclamations = reclamationService.getAllReclamations();
        return ResponseEntity.ok(reclamations);
    }

    @PutMapping("/reclamations/{id}/treat")
    public ResponseEntity<Reclamation> treatReclamation(@PathVariable Long id, @Validated @RequestBody ReclamationTreatmentDTO reclamationTreatmentDTO) {
        Reclamation treatedReclamation = reclamationService.treatReclamation(id, reclamationTreatmentDTO);
        notificationService.sendReclamationTreatedEmail(treatedReclamation.getEtudiant(), treatedReclamation);
        return ResponseEntity.ok(treatedReclamation);
    }
}
