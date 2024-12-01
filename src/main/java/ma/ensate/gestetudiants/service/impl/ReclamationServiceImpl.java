package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.dto.ReclamationDTO;
import ma.ensate.gestetudiants.dto.ReclamationTreatmentDTO;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.entity.Reclamation;
import ma.ensate.gestetudiants.enums.StatutReclamation;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.repository.ReclamationRepository;
import ma.ensate.gestetudiants.service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public Reclamation createReclamation(ReclamationDTO reclamationDTO) {
        Etudiant etudiant = etudiantRepository.findByEmailAndNumApogeeAndCin(
                reclamationDTO.getEmail(),
                reclamationDTO.getNumApogee(),
                reclamationDTO.getCin());

        if (etudiant == null) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec les informations fournies.");
        }

        Reclamation reclamation = new Reclamation();
        reclamation.setSujet(reclamationDTO.getSujet());
        reclamation.setMessage(reclamationDTO.getMessage());
        reclamation.setStatut(StatutReclamation.EN_ATTENTE);
        reclamation.setDateCreation(new Date());
        reclamation.setEtudiant(etudiant);

        return reclamationRepository.save(reclamation);
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public Reclamation treatReclamation(Long id, ReclamationTreatmentDTO reclamationTreatmentDTO) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamation not found with id " + id));

        reclamation.setReponse(reclamationTreatmentDTO.getReponse());
        reclamation.setStatut(StatutReclamation.TRAITEE);
        reclamation.setDateTraitement(new Date());

        return reclamationRepository.save(reclamation);
    }

}
