package ma.ensate.gestetudiants.dto.demande;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.enums.StatusDemande;
import ma.ensate.gestetudiants.enums.TypeDocument;

@Data
public class DemandeResponseDTO {
    private Long id;
    private TypeDocument typeDocument;
    private StatusDemande status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateCreation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateTraitement;
    private EtudiantBasicDTO etudiant;
    private String asyncErrorMessage;
}