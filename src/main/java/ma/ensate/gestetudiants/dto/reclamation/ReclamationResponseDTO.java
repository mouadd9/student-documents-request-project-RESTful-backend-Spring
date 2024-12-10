package ma.ensate.gestetudiants.dto.reclamation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.enums.StatusReclamation;

@Data
public class ReclamationResponseDTO {
    private Long id;
    private String sujet;
    private String message;
    private StatusReclamation status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateCreation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateTraitement;
    private String reponse;
    private EtudiantBasicDTO etudiant;
}