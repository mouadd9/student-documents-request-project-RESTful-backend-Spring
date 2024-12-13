package ma.ensate.gestetudiants.controller;

import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.dto.statistiques.StatistiquesDTO;
import ma.ensate.gestetudiants.enums.TypeDocument;
import ma.ensate.gestetudiants.service.DemandeService;
import ma.ensate.gestetudiants.service.DocumentGenerationService;
import ma.ensate.gestetudiants.service.NotificationService;
import ma.ensate.gestetudiants.service.ReclamationService;
import ma.ensate.gestetudiants.service.StatistiquesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DocumentGenerationService documentGenerationService;

    @Autowired
    private StatistiquesService statistiquesService;

    @GetMapping("/demandes")
    public ResponseEntity<List<DemandeResponseDTO>> getAllDemandes() {
        List<DemandeResponseDTO> demandes = demandeService.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }

    @PutMapping("/demandes/{id}/approve")
    public ResponseEntity<DemandeResponseDTO> approveDemande(@PathVariable Long id) {
        DemandeResponseDTO approvedDemande = demandeService.approveDemande(id);

        // Asynchronously generate PDF and send email
        CompletableFuture<byte[]> pdfFuture;

        if (approvedDemande.getTypeDocument() == TypeDocument.ATTESTATION_SCOLARITE) {
            pdfFuture = documentGenerationService.generateAttestation(approvedDemande.getEtudiant().getId());
        } else if (approvedDemande.getTypeDocument() == TypeDocument.RELEVE_NOTES) {
            pdfFuture = documentGenerationService.generateReleveDeNotes(approvedDemande.getEtudiant().getId());
        } else {
            throw new IllegalArgumentException("Type de document inconnu: " + approvedDemande.getTypeDocument());
        }

        pdfFuture.thenAccept(pdfBytes -> {
            notificationService.sendDemandeApprovedEmail(
                    approvedDemande.getEtudiant(),
                    approvedDemande,
                    pdfBytes
            );
        }).exceptionally(ex -> {
            // Log the exception or handle it accordingly
            ex.printStackTrace();
            return null;
        });

        return ResponseEntity.ok(approvedDemande);
    }

    @GetMapping("/demandes/{id}/pdf")
    public ResponseEntity<byte[]> getDemandePdf(@PathVariable Long id) {
        try {
            DemandeResponseDTO demande = demandeService.getDemandeById(id);
            CompletableFuture<byte[]> pdfFuture;

            if (demande.getTypeDocument() == TypeDocument.ATTESTATION_SCOLARITE) {
                pdfFuture = documentGenerationService.generateAttestation(demande.getEtudiant().getId());
            } else if (demande.getTypeDocument() == TypeDocument.RELEVE_NOTES) {
                pdfFuture = documentGenerationService.generateReleveDeNotes(demande.getEtudiant().getId());
            } else {
                return ResponseEntity.badRequest().body(null);
            }

            byte[] pdfBytes = pdfFuture.get(); // This will block until the PDF is generated

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = demande.getTypeDocument() == TypeDocument.ATTESTATION_SCOLARITE
                    ? "Attestation_Scolarite_" + demande.getEtudiant().getNom() + ".pdf"
                    : "Releve_de_Notes_" + demande.getEtudiant().getNom() + ".pdf";
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/demandes/{id}/reject")
    public ResponseEntity<DemandeResponseDTO> rejectDemande(@PathVariable Long id) {
        DemandeResponseDTO rejectedDemande = demandeService.rejectDemande(id);

        // Asynchronously send rejection email
        notificationService.sendDemandeRejectedEmail(
                rejectedDemande.getEtudiant(),
                rejectedDemande
        ).exceptionally(ex -> {
            // Log the exception or handle it accordingly
            ex.printStackTrace();
            return null;
        });

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

        // Asynchronously send treated reclamation email
        notificationService.sendReclamationTreatedEmail(
                treatedReclamation.getEtudiant(),
                treatedReclamation
        ).exceptionally(ex -> {
            // Log the exception or handle it accordingly
            ex.printStackTrace();
            return null;
        });

        return ResponseEntity.ok(treatedReclamation);
    }

    @GetMapping("/statistiques")
    public ResponseEntity<StatistiquesDTO> getStatistiques() {
        StatistiquesDTO stats = statistiquesService.getStatistiques();
        return ResponseEntity.ok(stats);
    }
}