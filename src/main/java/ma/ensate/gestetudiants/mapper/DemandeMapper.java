package ma.ensate.gestetudiants.mapper;

import ma.ensate.gestetudiants.dto.demande.DemandeRequestDTO;
import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.entity.Demande;

public class DemandeMapper {
    
    public static Demande toEntity(DemandeRequestDTO demandeDTO) {
        if (demandeDTO == null) {
            return null;
        }

        Demande demande = new Demande();
        demande.setTypeDocument(demandeDTO.getTypeDocument());
        
        return demande;
    }
    public static DemandeResponseDTO toDTO(Demande demande) {
        if (demande == null) {
            return null;
        }

        DemandeResponseDTO demandeDTO = new DemandeResponseDTO();
        demandeDTO.setId(demande.getId());
        demandeDTO.setTypeDocument(demande.getTypeDocument());
        demandeDTO.setStatut(demande.getStatut());
        demandeDTO.setDateCreation(demande.getDateCreation());
        demandeDTO.setDateTraitement(demande.getDateTraitement());
        demandeDTO.setEtudiant(EtudiantMapper.toBasicDTO(demande.getEtudiant()));
        
        return demandeDTO;
    }

}