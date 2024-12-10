package ma.ensate.gestetudiants.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensate.gestetudiants.dto.demande.DemandeRequestDTO;
import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.enums.StatusDemande;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.mapper.DemandeMapper;
import ma.ensate.gestetudiants.repository.DemandeRepository;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.service.DemandeService;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    DemandeRepository demandeRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    @Override
    public DemandeResponseDTO createDemande(final DemandeRequestDTO demandeDTO) {

        final Etudiant etudiant = etudiantRepository.findByEmailAndNumApogeeAndCin(
                demandeDTO.getEmail(),
                demandeDTO.getNumApogee(),
                demandeDTO.getCin());

        if (etudiant == null) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec les informations fournies.");
        }

        final Demande demande = DemandeMapper.toEntity(demandeDTO);
        demande.setStatus(StatusDemande.EN_ATTENTE);
        demande.setDateCreation(new Date());
        demande.setEtudiant(etudiant);

        final Demande savedDemande = demandeRepository.save(demande);
        return DemandeMapper.toDTO(savedDemande);
    }

    @Override
    public List<DemandeResponseDTO> getAllDemandes() {
        final List<Demande> demandes = demandeRepository.findAll();
        return demandes.stream()
                .map(DemandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DemandeResponseDTO approveDemande(final Long id) {
        final Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        demande.setStatus(StatusDemande.APPROVEE);
        demande.setDateTraitement(new Date());

        final Demande approvedDemande = demandeRepository.save(demande);
        return DemandeMapper.toDTO(approvedDemande);
    }

    @Override
    public DemandeResponseDTO rejectDemande(final Long id) {
        final Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        demande.setStatus(StatusDemande.REFUSEE);
        demande.setDateTraitement(new Date());

        final Demande rejectedDemande = demandeRepository.save(demande);
        return DemandeMapper.toDTO(rejectedDemande);
    }

    @Override
    public DemandeResponseDTO getDemandeById(final Long id) {
        final Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found with id " + id));

        return DemandeMapper.toDTO(demande);
    }

}