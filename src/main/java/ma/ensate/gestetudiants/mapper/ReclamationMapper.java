package ma.ensate.gestetudiants.mapper;

import ma.ensate.gestetudiants.dto.reclamation.ReclamationRequestDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.entity.Reclamation;

public class ReclamationMapper {

    public static Reclamation toEntity(ReclamationRequestDTO reclamationDTO) {
        if (reclamationDTO == null) {
            return null;
        }

        Reclamation reclamation = new Reclamation();
        reclamation.setSujet(reclamationDTO.getSujet());
        reclamation.setMessage(reclamationDTO.getMessage());
        reclamation.setReponse(reclamationDTO.getReponse());
        return reclamation;
    }

    public static ReclamationResponseDTO toDTO(Reclamation reclamation) {
        if (reclamation == null) {
            return null;
        }

        ReclamationResponseDTO dto = new ReclamationResponseDTO();
        dto.setId(reclamation.getId());
        dto.setSujet(reclamation.getSujet());
        dto.setMessage(reclamation.getMessage());
        dto.setStatut(reclamation.getStatut());
        dto.setDateCreation(reclamation.getDateCreation());
        dto.setDateTraitement(reclamation.getDateTraitement());
        dto.setReponse(reclamation.getReponse());
        dto.setEtudiant(EtudiantMapper.toBasicDTO(reclamation.getEtudiant()));
        return dto;
    }
}