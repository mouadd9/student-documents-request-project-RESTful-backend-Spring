package ma.ensate.gestetudiants.dto;

import lombok.Data;

@Data
public class ReclamationDTO {
    private String email;
    private int numApogee;
    private String cin;
    private String sujet;
    private String message;
}
