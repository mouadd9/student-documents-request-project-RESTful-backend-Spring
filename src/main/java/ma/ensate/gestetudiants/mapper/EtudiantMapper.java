package ma.ensate.gestetudiants.mapper;

import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.entity.Etudiant;

public class EtudiantMapper {

    public static EtudiantBasicDTO toBasicDTO(Etudiant etudiant) {
        EtudiantBasicDTO dto = new EtudiantBasicDTO();
        dto.setId(etudiant.getId());
        dto.setNom(etudiant.getNom());
        dto.setEmail(etudiant.getEmail());
        return dto;
    }
}