package ma.ensate.gestetudiants.dto;

import lombok.Data;
import ma.ensate.gestetudiants.enums.TypeDocument;

@Data
public class DemandeDTO {
    private String email;
    private int numApogee;
    private String cin;
    private TypeDocument typeDocument;
}
