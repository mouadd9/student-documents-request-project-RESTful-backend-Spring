package ma.ensate.gestetudiants.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ma.ensate.gestetudiants.dto.demande.DemandeRequestDTO;
import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.enums.StatusDemande;
import ma.ensate.gestetudiants.exception.EntityDuplicateException;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.mapper.DemandeMapper;
import ma.ensate.gestetudiants.repository.DemandeRepository;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.service.DemandeService;
import ma.ensate.gestetudiants.service.DocumentGenerationService;
import ma.ensate.gestetudiants.service.NotificationService;

@Service
public class DemandeServiceImpl implements DemandeService {

    private static final Logger logger = LoggerFactory.getLogger(DemandeServiceImpl.class);

    @Autowired
    DemandeRepository demandeRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    @Autowired
    private DocumentGenerationService documentGenerationService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public DemandeResponseDTO createDemande(final DemandeRequestDTO demandeDTO) {

        final Etudiant etudiant = etudiantRepository.findByEmailAndNumApogeeAndCin(
                demandeDTO.getEmail(),
                demandeDTO.getNumApogee(),
                demandeDTO.getCin());

        if (etudiant == null) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec les informations fournies.");
        }

        // Vérification d'une demande en attente existante
        if (demandeRepository.existsByEtudiantAndStatusAndTypeDocument(etudiant, StatusDemande.EN_ATTENTE,
                demandeDTO.getTypeDocument())) {
            throw new EntityDuplicateException("Une demande en attente existe déjà pour cet étudiant.");
        }

        final Demande demande = DemandeMapper.toEntity(demandeDTO);
        demande.setStatus(StatusDemande.EN_ATTENTE);
        demande.setDateCreation(new Date());
        demande.setEtudiant(etudiant);

        final Demande savedDemande = demandeRepository.save(demande);
        logger.info("Demande created successfully with ID: {}", savedDemande.getId());

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
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'ID: " + id));

        demande.setStatus(StatusDemande.EN_COURS);
        demande.setDateTraitement(new Date());
        demandeRepository.save(demande);
        logger.info("Demande with ID: {} updated to EN_COURS.", id);

        // Process approval asynchronously
        processApprovalAsync(demande);

        return DemandeMapper.toDTO(demande);
    }

    @Async
    public void processApprovalAsync(Demande demande) {
        documentGenerationService.generateDocument(demande.getTypeDocument(), demande.getEtudiant().getId())
                .thenCompose(pdfBytes -> notificationService.sendDemandeApprovedEmail(demande, pdfBytes))
                .thenAccept(aVoid -> {
                    demande.setStatus(StatusDemande.APPROVEE);
                    demande.setAsyncErrorMessage(null);
                    demandeRepository.save(demande);
                    logger.info("Demande with ID: {} approved successfully.", demande.getId());
                })
                .exceptionally(e -> {
                    String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    logger.error("Error processing approval for Demande ID {}: {}", demande.getId(), errorMessage);
                    demande.setStatus(StatusDemande.EN_ATTENTE);
                    demande.setAsyncErrorMessage(errorMessage);
                    demandeRepository.save(demande);
                    logger.info("Demande with ID: {} reverted to EN_ATTENTE due to error.", demande.getId());
                    return null;
                });
    }

    @Override
    public DemandeResponseDTO rejectDemande(final Long id) {
        final Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'ID: " + id));

        demande.setStatus(StatusDemande.EN_COURS);
        demande.setDateTraitement(new Date());
        demandeRepository.save(demande);
        logger.info("Demande with ID: {} updated to EN_COURS.", id);

        // Process rejection asynchronously
        processRejectionAsync(demande);

        return DemandeMapper.toDTO(demande);
    }

    @Async
    public CompletableFuture<Void> processRejectionAsync(Demande demande) {
        return notificationService.sendDemandeRejectedEmail(demande)
                .thenAccept(aVoid -> {
                    demande.setStatus(StatusDemande.REFUSEE);
                    demande.setAsyncErrorMessage(null); // Effacer les erreurs précédentes
                    demandeRepository.save(demande);
                    logger.info("Demande with ID: {} rejected successfully.", demande.getId());
                })
                .exceptionally(e -> {
                    String errorMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                    logger.error("Error during asynchronous processing of request ID {}: {}", demande.getId(),
                            errorMessage);
                    demande.setStatus(StatusDemande.EN_ATTENTE);
                    demande.setAsyncErrorMessage(errorMessage); // Stocker le message d'erreur
                    demandeRepository.save(demande);
                    logger.info("Demande with ID: {} returned to EN_ATTENTE.", demande.getId());
                    return null;
                });
    }

    @Override
    public DemandeResponseDTO getDemandeById(final Long id) {
        return demandeRepository.findById(id)
                .map(DemandeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'ID: " + id));
    }
}