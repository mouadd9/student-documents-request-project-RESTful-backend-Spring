package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.dto.demande.DemandeRequestDTO;
import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.enums.StatutDemande;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.mapper.DemandeMapper;
import ma.ensate.gestetudiants.repository.DemandeRepository;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    DemandeRepository demandeRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    @Override
    public DemandeResponseDTO createDemande(DemandeRequestDTO demandeDTO) {

        Etudiant etudiant = etudiantRepository.findByEmailAndNumApogeeAndCin(
                demandeDTO.getEmail(),
                demandeDTO.getNumApogee(),
                demandeDTO.getCin());

        if (etudiant == null) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec les informations fournies.");
        }

        Demande demande = DemandeMapper.toEntity(demandeDTO);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setDateCreation(new Date());
        demande.setEtudiant(etudiant);

        Demande savedDemande = demandeRepository.save(demande);
        return DemandeMapper.toDTO(savedDemande);
    }

    @Override
    public List<DemandeResponseDTO> getAllDemandes() {
        List<Demande> demandes = demandeRepository.findAll();
        return demandes.stream()
                .map(DemandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DemandeResponseDTO approveDemande(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        demande.setStatut(StatutDemande.APPROVEE);
        demande.setDateTraitement(new Date());

        Demande approvedDemande = demandeRepository.save(demande);
        return DemandeMapper.toDTO(approvedDemande);
    }

    @Override
    public DemandeResponseDTO rejectDemande(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        demande.setStatut(StatutDemande.REFUSEE);
        demande.setDateTraitement(new Date());

        Demande rejectedDemande = demandeRepository.save(demande);
        return DemandeMapper.toDTO(rejectedDemande);
    }

    @Override
    public DemandeResponseDTO getDemandeById(Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        return DemandeMapper.toDTO(demande);
    }

}