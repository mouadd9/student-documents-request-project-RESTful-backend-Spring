package ma.ensate.gestetudiants.service;

public interface DocumentGenerationService {
    byte[] generateAttestation(Long etudiantId) throws Exception;
    byte[] generateReleveDeNotes(Long etudiantId) throws Exception;
}