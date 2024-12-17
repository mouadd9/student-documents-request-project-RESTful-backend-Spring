package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Reclamation;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<Void> sendDemandeApprovedEmail(Demande demande, byte[] documentBytes);
    CompletableFuture<Void> sendDemandeRejectedEmail(Demande demande);
    CompletableFuture<Void> sendReclamationTreatedEmail(Reclamation reclamation);
}