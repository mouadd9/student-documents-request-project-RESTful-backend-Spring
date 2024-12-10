package ma.ensate.gestetudiants.dto.statistiques;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StatistiquesDTO {
    private Long approvedDemandes;
    private Long rejectedDemandes;
    private Long pendingDemandes;
    private Long pendingReclamations;

    private Double averageDemandesProcessingTimeDays;
    private Double averageReclamationsProcessingTimeDays;

    // Aperçu mensuel par type de demandes
    private List<String> monthlyLabels;
    private Map<String, List<Integer>> monthlyData;

    // Tendances hebdomadaires des demandes et réclamations
    private List<String> weeklyLabels;
    private Map<String, List<Long>> weeklyData;
}