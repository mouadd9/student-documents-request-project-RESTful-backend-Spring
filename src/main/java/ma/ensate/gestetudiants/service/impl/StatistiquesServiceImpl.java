package ma.ensate.gestetudiants.service.impl;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.ensate.gestetudiants.dto.statistiques.StatistiquesDTO;
import ma.ensate.gestetudiants.enums.StatusDemande;
import ma.ensate.gestetudiants.enums.StatusReclamation;
import ma.ensate.gestetudiants.enums.TypeDocument;
import ma.ensate.gestetudiants.repository.DemandeRepository;
import ma.ensate.gestetudiants.repository.ReclamationRepository;
import ma.ensate.gestetudiants.service.StatistiquesService;

@Service
public class StatistiquesServiceImpl implements StatistiquesService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Override
    public StatistiquesDTO getStatistiques() {
        final StatistiquesDTO stats = new StatistiquesDTO();

        // Statistiques existantes
        final Long approvedRequests = demandeRepository.countByStatus(StatusDemande.APPROVEE);
        final Long rejectedRequests = demandeRepository.countByStatus(StatusDemande.REFUSEE);
        final Long pendingRequests = demandeRepository.countByStatus(StatusDemande.EN_ATTENTE);
        final Long pendingReclamations = reclamationRepository.countByStatus(StatusReclamation.EN_ATTENTE);

        stats.setApprovedDemandes(approvedRequests);
        stats.setRejectedDemandes(rejectedRequests);
        stats.setPendingDemandes(pendingRequests);
        stats.setPendingReclamations(pendingReclamations);

        final Double avgDemandesTime = demandeRepository.calculateAverageDemandesProcessingTime();
        stats.setAverageDemandesProcessingTimeDays(avgDemandesTime != null ? avgDemandesTime : 0.0);

        final Double avgReclamationsTime = reclamationRepository.calculateAverageReclamationsProcessingTime();
        stats.setAverageReclamationsProcessingTimeDays(avgReclamationsTime != null ? avgReclamationsTime : 0.0);

        // Aperçu mensuel avec abréviations
        final List<Object[]> monthlyCounts = demandeRepository.countDemandesPerMonthAndType();
        final Map<String, Map<TypeDocument, Integer>> monthlyDataMap = new TreeMap<>();

        for (final Object[] record : monthlyCounts) {
            final Integer month = ((Number) record[0]).intValue();
            final TypeDocument type = (TypeDocument) record[1];
            final Long count = ((Number) record[2]).longValue();

            final String monthAbbreviated = getAbbreviatedMonthName(month);

            monthlyDataMap.putIfAbsent(monthAbbreviated, new EnumMap<>(TypeDocument.class));
            monthlyDataMap.get(monthAbbreviated).put(type, count.intValue());
        }

        final List<String> monthlyLabels = new ArrayList<>(monthlyDataMap.keySet());
        final List<Integer> attestationsData = new ArrayList<>();
        final List<Integer> relevesData = new ArrayList<>();

        for (final String month : monthlyLabels) {
            final Map<TypeDocument, Integer> typeCounts = monthlyDataMap.get(month);
            attestationsData.add(typeCounts.getOrDefault(TypeDocument.ATTESTATION_SCOLARITE, 0));
            relevesData.add(typeCounts.getOrDefault(TypeDocument.RELEVE_NOTES, 0));
        }

        stats.setMonthlyLabels(monthlyLabels);
        final Map<String, List<Integer>> monthlyData = new HashMap<>();
        monthlyData.put("Attestations", attestationsData);
        monthlyData.put("Relevés de notes", relevesData);
        stats.setMonthlyData(monthlyData);

        // Tendances hebdomadaires
        // Demandes par jour
        final List<Object[]> weeklyDemandesCounts = demandeRepository.countDemandesPerDayOfWeek();
        final Map<String, Long> weeklyDemandesMap = new HashMap<>();
        for (final Object[] record : weeklyDemandesCounts) {
            final String dayName = (String) record[0];
            final Long count = ((Number) record[2]).longValue();
            weeklyDemandesMap.put(dayName, count);
        }

        // Reclamations par jour
        final List<Object[]> weeklyReclamationsCounts = reclamationRepository.countReclamationsPerDayOfWeek();
        final Map<String, Long> weeklyReclamationsMap = new HashMap<>();
        for (final Object[] record : weeklyReclamationsCounts) {
            final String dayName = (String) record[0];
            final Long count = ((Number) record[1]).longValue();
            weeklyReclamationsMap.put(dayName, count);
        }

        final List<String> weeklyLabelsEn = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
                "Sunday");
        final List<String> weeklyLabelsFr = Arrays.asList("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim");

        final List<Long> demandesData = new ArrayList<>();
        final List<Long> reclamationsData = new ArrayList<>();

        for (int i = 0; i < weeklyLabelsEn.size(); i++) {
            final String dayEnglish = weeklyLabelsEn.get(i);
            final long demandesCount = weeklyDemandesMap.getOrDefault(dayEnglish, 0L);
            final long reclamationsCount = weeklyReclamationsMap.getOrDefault(dayEnglish, 0L);

            demandesData.add(demandesCount);
            reclamationsData.add(reclamationsCount);
        }

        stats.setWeeklyLabels(weeklyLabelsFr);
        final Map<String, List<Long>> weeklyData = new HashMap<>();
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
    private String getAbbreviatedMonthName(final int month) {
        final String[] shortMonths = new DateFormatSymbols(Locale.FRENCH).getShortMonths();
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