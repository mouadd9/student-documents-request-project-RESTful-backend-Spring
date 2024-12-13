package ma.ensate.gestetudiants.service;

import java.util.concurrent.CompletableFuture;

public interface DocumentGenerationService {
    CompletableFuture<byte[]> generateAttestation(Long etudiantId);
    CompletableFuture<byte[]> generateReleveDeNotes(Long etudiantId);
}