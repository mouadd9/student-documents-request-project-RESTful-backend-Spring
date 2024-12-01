package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.dto.ReclamationDTO;
import ma.ensate.gestetudiants.dto.ReclamationTreatmentDTO;
import ma.ensate.gestetudiants.entity.Reclamation;

import java.util.List;

public interface ReclamationService {
    Reclamation createReclamation(ReclamationDTO reclamationDTO);
    List<Reclamation> getAllReclamations();
    Reclamation treatReclamation(Long id, ReclamationTreatmentDTO reclamationTreatmentDTO);
}
