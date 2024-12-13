package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<Void> sendDemandeApprovedEmail(EtudiantBasicDTO etudiant, DemandeResponseDTO demande, byte[] documentBytes);
    CompletableFuture<Void> sendDemandeRejectedEmail(EtudiantBasicDTO etudiant, DemandeResponseDTO demande);
    CompletableFuture<Void> sendReclamationTreatedEmail(EtudiantBasicDTO etudiant, ReclamationResponseDTO reclamation);
}