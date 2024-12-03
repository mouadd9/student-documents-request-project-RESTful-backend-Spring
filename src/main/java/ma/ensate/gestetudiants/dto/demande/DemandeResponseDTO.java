package ma.ensate.gestetudiants.dto.demande;

import lombok.Data;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.enums.StatutDemande;
import ma.ensate.gestetudiants.enums.TypeDocument;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class DemandeResponseDTO {
    private Long id;
    private TypeDocument typeDocument;
    private StatutDemande statut;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateCreation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateTraitement;
    private EtudiantBasicDTO etudiant;
}