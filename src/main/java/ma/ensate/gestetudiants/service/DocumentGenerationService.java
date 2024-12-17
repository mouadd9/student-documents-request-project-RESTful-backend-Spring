package ma.ensate.gestetudiants.service;

import java.util.concurrent.CompletableFuture;

import ma.ensate.gestetudiants.enums.TypeDocument;

public interface DocumentGenerationService {
    CompletableFuture<byte[]> generateDocument(TypeDocument type, Long etudiantId);
    CompletableFuture<byte[]> generateAttestation(Long etudiantId);
    CompletableFuture<byte[]> generateReleveDeNotes(Long etudiantId);
}