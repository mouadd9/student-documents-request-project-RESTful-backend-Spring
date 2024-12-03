package ma.ensate.gestetudiants.dto.reclamation;

import lombok.Data;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.enums.StatutReclamation;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ReclamationResponseDTO {
    private Long id;
    private String sujet;
    private String message;
    private StatutReclamation statut;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateCreation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateTraitement;
    private String reponse;
    private EtudiantBasicDTO etudiant;
}