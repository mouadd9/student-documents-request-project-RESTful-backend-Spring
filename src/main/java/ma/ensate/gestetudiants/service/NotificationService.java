package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;

public interface NotificationService {
    void sendDemandeApprovedEmail(EtudiantBasicDTO etudiant, DemandeResponseDTO demande, byte[] documentBytes);
    void sendDemandeRejectedEmail(EtudiantBasicDTO etudiant, DemandeResponseDTO demande);
    void sendReclamationTreatedEmail(EtudiantBasicDTO etudiant, ReclamationResponseDTO reclamation);
}