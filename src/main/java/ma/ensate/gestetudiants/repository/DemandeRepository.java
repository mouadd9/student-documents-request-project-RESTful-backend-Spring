package ma.ensate.gestetudiants.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.enums.StatusDemande;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {

    Long countByStatus(StatusDemande status);

    @Query(value = "SELECT AVG(DATEDIFF(d.date_traitement, d.date_creation)) FROM demande d WHERE d.date_traitement IS NOT NULL", nativeQuery = true)
    Double calculateAverageDemandesProcessingTime();

    // Monthly Attestations and Relevés de notes
    @Query("SELECT FUNCTION('MONTH', d.dateCreation), d.typeDocument, COUNT(d) FROM Demande d WHERE FUNCTION('YEAR', d.dateCreation) = FUNCTION('YEAR', CURRENT_DATE) GROUP BY FUNCTION('MONTH', d.dateCreation), d.typeDocument ORDER BY FUNCTION('MONTH', d.dateCreation)")
    List<Object[]> countDemandesPerMonthAndType();

    // Demandes per day of the current week
    @Query(value = """
            SELECT
                DAYNAME(d.date_creation) AS dayName,
                DAYOFWEEK(d.date_creation) AS dayOfWeek,
                COUNT(*)
            FROM demande d
            WHERE WEEK(d.date_creation, 1) = WEEK(CURDATE(), 1)
              AND YEAR(d.date_creation) = YEAR(CURDATE())
            GROUP BY dayName, dayOfWeek
            ORDER BY dayOfWeek
            """, nativeQuery = true)
    List<Object[]> countDemandesPerDayOfWeek();
}