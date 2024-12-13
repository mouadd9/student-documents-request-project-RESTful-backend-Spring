package ma.ensate.gestetudiants.repository;

import ma.ensate.gestetudiants.entity.Etudiant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Etudiant findByEmailAndNumApogeeAndCin(String email, Integer numApogee, String cin);

    @Query("SELECT e FROM Etudiant e LEFT JOIN FETCH e.notes WHERE e.id = :id")
    Optional<Etudiant> findByIdWithNotes(@Param("id") Long id);
}
