package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.dto.demande.DemandeRequestDTO;
import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;

import java.util.List;

public interface DemandeService {
    DemandeResponseDTO createDemande(DemandeRequestDTO demandeDTO);

    List<DemandeResponseDTO> getAllDemandes();

    DemandeResponseDTO getDemandeById(Long id);

    DemandeResponseDTO approveDemande(Long id);

    DemandeResponseDTO rejectDemande(Long id);

}
