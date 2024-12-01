package ma.ensate.gestetudiants.repository;

import ma.ensate.gestetudiants.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNomUtilisateur(String nomUtilisateur);
}