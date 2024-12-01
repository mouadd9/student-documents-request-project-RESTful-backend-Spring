package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.dto.DemandeDTO;
import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.enums.StatutDemande;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.repository.DemandeRepository;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    DemandeRepository demandeRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    @Override
    public Demande createDemande(DemandeDTO demandeDTO) {
        Etudiant etudiant = etudiantRepository.findByEmailAndNumApogeeAndCin(
                demandeDTO.getEmail(),
                demandeDTO.getNumApogee(),
                demandeDTO.getCin()
        );

        if (etudiant == null) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec les informations fournies.");
        }

        Demande demande = new Demande();
        demande.setTypeDocument(demandeDTO.getTypeDocument());
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setDateCreation(new Date());
        demande.setEtudiant(etudiant);

        return demandeRepository.save(demande);
    }

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public Demande approveDemande(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        demande.setStatut(StatutDemande.APPROVEE);
        demande.setDateTraitement(new Date());

        return demandeRepository.save(demande);
    }

    @Override
    public Demande rejectDemande(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        demande.setStatut(StatutDemande.REFUSEE);
        demande.setDateTraitement(new Date());

        return demandeRepository.save(demande);
    }


}
