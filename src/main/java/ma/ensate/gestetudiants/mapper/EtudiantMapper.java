package ma.ensate.gestetudiants.mapper;

import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantDetailedDTO;
import ma.ensate.gestetudiants.entity.Etudiant;

public class EtudiantMapper {

    public static EtudiantBasicDTO toBasicDTO(Etudiant etudiant) {
        EtudiantBasicDTO dto = new EtudiantBasicDTO();
        dto.setId(etudiant.getId());
        dto.setNom(etudiant.getNom());
        dto.setEmail(etudiant.getEmail());
        return dto;
    }

    public static EtudiantDetailedDTO toDetailedDTO(Etudiant etudiant) {
        EtudiantDetailedDTO dto = new EtudiantDetailedDTO();
        dto.setId(etudiant.getId());
        dto.setNom(etudiant.getNom());
        dto.setEmail(etudiant.getEmail());
        dto.setNumApogee(etudiant.getNumApogee());
        dto.setCin(etudiant.getCin());
        dto.setDateNaissance(etudiant.getDateNaissance());
        dto.setLieuNaissance(etudiant.getLieuNaissance());
        dto.setNationalite(etudiant.getNationalite());
        dto.setFiliere(etudiant.getFiliere());
        dto.setNiveau(etudiant.getNiveau());
        dto.setAnneeUniversitaire(etudiant.getAnneeUniversitaire());
        return dto;
    }
}