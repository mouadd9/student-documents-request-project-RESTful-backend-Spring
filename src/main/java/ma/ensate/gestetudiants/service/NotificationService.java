package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.entity.Reclamation;

public interface NotificationService {
    void sendDemandeApprovedEmail(Etudiant etudiant, Demande demande);
    void sendDemandeRejectedEmail(Etudiant etudiant, Demande demande);
    void sendReclamationTreatedEmail(Etudiant etudiant, Reclamation reclamation);
}