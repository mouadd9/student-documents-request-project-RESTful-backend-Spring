package ma.ensate.gestetudiants.dto.demande;

import lombok.Data;
import ma.ensate.gestetudiants.enums.TypeDocument;

@Data
public class DemandeRequestDTO {
    private String email;
    private int numApogee;
    private String cin;
    
    private TypeDocument typeDocument;
}
