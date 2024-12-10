package ma.ensate.gestetudiants.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensate.gestetudiants.dto.reclamation.ReclamationRequestDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.entity.Reclamation;
import ma.ensate.gestetudiants.enums.StatusReclamation;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.mapper.ReclamationMapper;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.repository.ReclamationRepository;
import ma.ensate.gestetudiants.service.ReclamationService;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public ReclamationResponseDTO createReclamation(final ReclamationRequestDTO reclamationDTO) {
        final Etudiant etudiant = etudiantRepository.findByEmailAndNumApogeeAndCin(
                reclamationDTO.getEmail(),
                reclamationDTO.getNumApogee(),
                reclamationDTO.getCin());

        if (etudiant == null) {
            throw new ResourceNotFoundException("Étudiant non trouvé avec les informations fournies.");
        }

        final Reclamation reclamation = ReclamationMapper.toEntity(reclamationDTO);
        reclamation.setStatus(StatusReclamation.EN_ATTENTE);
        reclamation.setDateCreation(new Date());
        reclamation.setEtudiant(etudiant);

        final Reclamation savedReclamation = reclamationRepository.save(reclamation);
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
    public ReclamationResponseDTO treatReclamation(final Long id, final ReclamationResponseDTO reclamationDTO) {
        final Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamation not found with id " + id));

        reclamation.setReponse(reclamationDTO.getReponse());
        reclamation.setStatus(StatusReclamation.TRAITEE);
        reclamation.setDateTraitement(new Date());

        final Reclamation treatedReclamation = reclamationRepository.save(reclamation);
        return ReclamationMapper.toDTO(treatedReclamation);
    }
}