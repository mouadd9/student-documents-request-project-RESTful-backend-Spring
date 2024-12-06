package ma.ensate.gestetudiants.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensate.gestetudiants.dto.statistiques.StatistiquesDTO;
import ma.ensate.gestetudiants.enums.StatutDemande;
import ma.ensate.gestetudiants.enums.TypeDocument;
import ma.ensate.gestetudiants.repository.DemandeRepository;
import ma.ensate.gestetudiants.repository.ReclamationRepository;
import ma.ensate.gestetudiants.service.StatistiquesService;

import java.text.DateFormatSymbols;
import java.util.*;

@Service
public class StatistiquesServiceImpl implements StatistiquesService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Override
    public StatistiquesDTO getStatistiques() {
        StatistiquesDTO stats = new StatistiquesDTO();

        // Statistiques existantes
        Long totalRequests = demandeRepository.count();
        Long approvedRequests = demandeRepository.countByStatut(StatutDemande.APPROVEE);
        Long rejectedRequests = demandeRepository.countByStatut(StatutDemande.REFUSEE);
        Long pendingRequests = demandeRepository.countByStatut(StatutDemande.EN_ATTENTE);

        stats.setApprovedDemandes(approvedRequests);
        stats.setRejectedDemandes(rejectedRequests);
        stats.setPendingDemandes(pendingRequests);

        if (totalRequests > 0) {
            stats.setApprovalRate((approvedRequests * 100.0) / totalRequests);
            stats.setRejectionRate((rejectedRequests * 100.0) / totalRequests);
        } else {
            stats.setApprovalRate(0.0);
            stats.setRejectionRate(0.0);
        }

        Double avgDemandesTime = demandeRepository.calculateAverageDemandesProcessingTime();
        stats.setAverageDemandesProcessingTimeDays(avgDemandesTime != null ? avgDemandesTime : 0.0);

        Double avgReclamationsTime = reclamationRepository.calculateAverageReclamationsProcessingTime();
        stats.setAverageReclamationsProcessingTimeDays(avgReclamationsTime != null ? avgReclamationsTime : 0.0);

        Double satisfactionRate = reclamationRepository.calculateSatisfactionRate();
        stats.setSatisfactionRate(satisfactionRate != null ? satisfactionRate : 0.0);

        // Aperçu mensuel avec abréviations
        List<Object[]> monthlyCounts = demandeRepository.countDemandesPerMonthAndType();
        Map<String, Map<TypeDocument, Integer>> monthlyDataMap = new TreeMap<>();

        for (Object[] record : monthlyCounts) {
            Integer month = ((Number) record[0]).intValue();
            TypeDocument type = (TypeDocument) record[1];
            Long count = ((Number) record[2]).longValue();

            String monthAbbreviated = getAbbreviatedMonthName(month);

            monthlyDataMap.putIfAbsent(monthAbbreviated, new EnumMap<>(TypeDocument.class));
            monthlyDataMap.get(monthAbbreviated).put(type, count.intValue());
        }

        List<String> monthlyLabels = new ArrayList<>(monthlyDataMap.keySet());
        List<Integer> attestationsData = new ArrayList<>();
        List<Integer> relevesData = new ArrayList<>();

        for (String month : monthlyLabels) {
            Map<TypeDocument, Integer> typeCounts = monthlyDataMap.get(month);
            attestationsData.add(typeCounts.getOrDefault(TypeDocument.ATTESTATION_SCOLARITE, 0));
            relevesData.add(typeCounts.getOrDefault(TypeDocument.RELEVE_NOTES, 0));
        }

        stats.setMonthlyLabels(monthlyLabels);
        Map<String, List<Integer>> monthlyData = new HashMap<>();
        monthlyData.put("Attestations", attestationsData);
        monthlyData.put("Relevés de notes", relevesData);
        stats.setMonthlyData(monthlyData);

        // Tendances hebdomadaires
        // Demandes par jour
        List<Object[]> weeklyDemandesCounts = demandeRepository.countDemandesPerDayOfWeek();
        Map<String, Long> weeklyDemandesMap = new HashMap<>();
        for (Object[] record : weeklyDemandesCounts) {
            String dayName = (String) record[0];
            Long count = ((Number) record[2]).longValue();
            weeklyDemandesMap.put(dayName, count);
        }

        // Reclamations par jour
        List<Object[]> weeklyReclamationsCounts = reclamationRepository.countReclamationsPerDayOfWeek();
        Map<String, Long> weeklyReclamationsMap = new HashMap<>();
        for (Object[] record : weeklyReclamationsCounts) {
            String dayName = (String) record[0];
            Long count = ((Number) record[1]).longValue();
            weeklyReclamationsMap.put(dayName, count);
        }

        List<String> weeklyLabelsEn = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
                "Sunday");
        List<String> weeklyLabelsFr = Arrays.asList("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim");

        List<Long> demandesData = new ArrayList<>();
        List<Long> reclamationsData = new ArrayList<>();

        for (int i = 0; i < weeklyLabelsEn.size(); i++) {
            String dayEnglish = weeklyLabelsEn.get(i);
            long demandesCount = weeklyDemandesMap.getOrDefault(dayEnglish, 0L);
            long reclamationsCount = weeklyReclamationsMap.getOrDefault(dayEnglish, 0L);

            demandesData.add(demandesCount);
            reclamationsData.add(reclamationsCount);
        }

        stats.setWeeklyLabels(weeklyLabelsFr);
        Map<String, List<Long>> weeklyData = new HashMap<>();
        weeklyData.put("Demandes", demandesData);
        weeklyData.put("Réclamations", reclamationsData);
        stats.setWeeklyData(weeklyData);

        return stats;
    }

    /**
     * Retourne le nom abrégé du mois en français.
     *
     * @param month Numéro du mois (1 - 12)
     * @return Nom abrégé du mois ou "Inconnu" si le mois est invalide
     */
    private String getAbbreviatedMonthName(int month) {
        String[] shortMonths = new DateFormatSymbols(Locale.FRENCH).getShortMonths();
        if (month >= 1 && month <= 12) {
            String abbreviated = shortMonths[month - 1];
            // Supprimer les espaces ou caractères indésirables et ajouter un point si nécessaire
            abbreviated = abbreviated.trim();
            if (!abbreviated.endsWith(".")) {
                abbreviated += ".";
            }
            return abbreviated;
        } else {
            return "Inconnu";
        }
    }
}