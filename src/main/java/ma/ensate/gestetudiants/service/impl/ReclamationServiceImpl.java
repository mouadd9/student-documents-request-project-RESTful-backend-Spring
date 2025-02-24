package ma.ensate.gestetudiants.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.ensate.gestetudiants.dto.reclamation.ReclamationRequestDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.entity.Reclamation;
import ma.ensate.gestetudiants.enums.StatusReclamation;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.mapper.ReclamationMapper;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.repository.ReclamationRepository;
import ma.ensate.gestetudiants.service.NotificationService;
import ma.ensate.gestetudiants.service.ReclamationService;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    private static final Logger logger = LoggerFactory.getLogger(DemandeServiceImpl.class);

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public ReclamationResponseDTO createReclamation(final ReclamationRequestDTO reclamationDTO) {

        // Recherche de l'étudiant
        final Etudiant etudiant = etudiantRepository.findByEmailAndNumApogeeAndCin(
                reclamationDTO.getEmail(),
                reclamationDTO.getNumApogee(),
                reclamationDTO.getCin());

        if (etudiant == null) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec les informations fournies.");
        }

        
        // Création de la réclamation
        final Reclamation reclamation = ReclamationMapper.toEntity(reclamationDTO);
        reclamation.setStatus(StatusReclamation.EN_ATTENTE);
        reclamation.setDateCreation(new Date());
        reclamation.setEtudiant(etudiant);

        final Reclamation savedReclamation = reclamationRepository.save(reclamation);
        logger.info("Reclamation created successfully with ID: {}", savedReclamation.getId());

        return ReclamationMapper.toDTO(savedReclamation);
    }

    @Override
    public List<ReclamationResponseDTO> getAllReclamations() {
        final List<Reclamation> reclamations = reclamationRepository.findAll();
        return reclamations.stream()
                .map(ReclamationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReclamationResponseDTO treatReclamation(final Long id, final ReclamationRequestDTO reclamationDTO) {

        // Recherche de la réclamation
        final Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réclamation non trouvée avec l'id " + id));

        // Traitement de la réclamation
        reclamation.setReponse(reclamationDTO.getReponse());
        reclamation.setDateTraitement(new Date());

        notificationService.sendReclamationTreatedEmail(reclamation);

        reclamation.setStatus(StatusReclamation.TRAITEE);
        reclamationRepository.save(reclamation);

        logger.info("Reclamation with ID: {} processed successfully.", id);

        return ReclamationMapper.toDTO(reclamation);

    }

    @Override
    public ReclamationResponseDTO getReclamationById(Long id) {
        return reclamationRepository.findById(id)
                .map(ReclamationMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Réclamation non trouvée avec l'id " + id));
    }
}