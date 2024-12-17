package ma.ensate.gestetudiants.service;


import ma.ensate.gestetudiants.enums.TypeDocument;

public interface DocumentGenerationService {
    byte[] generateDocument(TypeDocument type, Long etudiantId);
    byte[] generateAttestation(Long etudiantId);
    byte[] generateReleveDeNotes(Long etudiantId);
}