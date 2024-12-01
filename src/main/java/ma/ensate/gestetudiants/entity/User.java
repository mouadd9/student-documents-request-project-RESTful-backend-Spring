package ma.ensate.gestetudiants.entity;

import jakarta.persistence.*;
import lombok.Data;
import ma.ensate.gestetudiants.enums.Role;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomUtilisateur;
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Role role;
}
