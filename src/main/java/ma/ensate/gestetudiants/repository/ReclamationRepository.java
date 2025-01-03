package ma.ensate.gestetudiants.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.entity.Reclamation;
import ma.ensate.gestetudiants.enums.StatusReclamation;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    @Query(value = "SELECT AVG(DATEDIFF(r.date_traitement, r.date_creation)) FROM reclamation r WHERE r.date_traitement IS NOT NULL", nativeQuery = true)
    Double calculateAverageReclamationsProcessingTime();

    @Query("SELECT (COUNT(r) * 100.0) / (SELECT COUNT(r2) FROM Reclamation r2) FROM Reclamation r WHERE r.status = 'TRAITEE'")
    Double calculateSatisfactionRate();

    // Reclamations per day of the current week
    @Query(value = """
            SELECT
                DAYNAME(r.date_creation) AS dayName,
                DAYOFWEEK(r.date_creation) AS dayOfWeek,
                COUNT(*)
            FROM reclamation r
            WHERE WEEK(r.date_creation, 1) = WEEK(CURDATE(), 1)
              AND YEAR(r.date_creation) = YEAR(CURDATE())
            GROUP BY dayName, dayOfWeek
            ORDER BY dayOfWeek
            """, nativeQuery = true)
    List<Object[]> countReclamationsPerDayOfWeek();

    Long countByStatus(StatusReclamation status);

    boolean existsByEtudiantAndStatus(Etudiant etudiant, StatusReclamation enAttente);
}