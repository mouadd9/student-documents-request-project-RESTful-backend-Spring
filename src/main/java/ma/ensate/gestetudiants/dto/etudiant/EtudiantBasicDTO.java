package ma.ensate.gestetudiants.dto.etudiant;

import lombok.Data;

@Data
public class EtudiantBasicDTO {
    private Long id;
    private String nom;
    private String email;
}