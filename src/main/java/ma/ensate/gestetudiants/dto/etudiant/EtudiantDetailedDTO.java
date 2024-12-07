package ma.ensate.gestetudiants.dto.etudiant;

import java.util.Date;

import lombok.Data;

@Data
public class EtudiantDetailedDTO {
    private Long id;
    private String nom;
    private String email;
    private int numApogee;
    private String cin;
    private Date dateNaissance;
    private String lieuNaissance;
    private String nationalite;
    private String filiere;
    private String niveau;
    private String anneeUniversitaire;
}
