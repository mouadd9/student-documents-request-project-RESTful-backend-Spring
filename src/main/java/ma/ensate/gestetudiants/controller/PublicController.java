package ma.ensate.gestetudiants.controller;

import ma.ensate.gestetudiants.dto.DemandeDTO;
import ma.ensate.gestetudiants.dto.ReclamationDTO;
import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Reclamation;
import ma.ensate.gestetudiants.service.DemandeService;
import ma.ensate.gestetudiants.service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublicController {

    @Autowired
    DemandeService demandeService;

    @Autowired
    ReclamationService reclamationService;

    @PostMapping("/demandes")
    public ResponseEntity<Demande> createDemande(@Validated @RequestBody DemandeDTO demandeDTO) {
        Demande demande = demandeService.createDemande(demandeDTO);
        return new ResponseEntity<>(demande, HttpStatus.CREATED);
    }

    @PostMapping("/reclamations")
    public ResponseEntity<Reclamation> createReclamation(@Validated @RequestBody ReclamationDTO reclamationDTO) {
        Reclamation reclamation = reclamationService.createReclamation(reclamationDTO);
        return new ResponseEntity<>(reclamation, HttpStatus.CREATED);
    }
}
