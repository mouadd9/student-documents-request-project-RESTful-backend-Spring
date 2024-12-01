package ma.ensate.gestetudiants.repository;

import ma.ensate.gestetudiants.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Etudiant findByEmailAndNumApogeeAndCin(String email, Integer numApogee, String cin);
}
