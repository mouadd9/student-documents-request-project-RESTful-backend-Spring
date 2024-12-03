package ma.ensate.gestetudiants.dto.reclamation;

import lombok.Data;

@Data
public class ReclamationRequestDTO {
    private String email;
    private int numApogee;
    private String cin;
    
    private String sujet;
    private String message;
    private String reponse;
}
