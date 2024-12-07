package ma.ensate.gestetudiants.service;

public interface DocumentGenerationService {
    byte[] generateDocument(Long demandeId) throws Exception;
}
