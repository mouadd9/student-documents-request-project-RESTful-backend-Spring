package ma.ensate.gestetudiants.entity;

import jakarta.persistence.*;
import lombok.Data;
import ma.ensate.gestetudiants.enums.StatutDemande;
import ma.ensate.gestetudiants.enums.TypeDocument;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Data
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeDocument typeDocument;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateCreation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateTraitement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
}
