package ma.ensate.gestetudiants.service;

import ma.ensate.gestetudiants.dto.DemandeDTO;
import ma.ensate.gestetudiants.entity.Demande;

import java.util.List;

public interface DemandeService {
    Demande createDemande(DemandeDTO demandeDTO);
    List<Demande> getAllDemandes();
    Demande approveDemande(Long id);
    Demande rejectDemande(Long id);

}
