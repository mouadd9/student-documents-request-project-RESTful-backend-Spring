package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.dto.reclamation.ReclamationRequestDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;

import java.util.List;

public interface ReclamationService {
    ReclamationResponseDTO createReclamation(ReclamationRequestDTO reclamationDTO);

    List<ReclamationResponseDTO> getAllReclamations();

    ReclamationResponseDTO treatReclamation(Long id, ReclamationResponseDTO reclamationDTO);

    ReclamationResponseDTO getReclamationById(Long id);
}