package ma.ensate.gestetudiants;

import ma.ensate.gestetudiants.entity.User;
import ma.ensate.gestetudiants.enums.Role;
import ma.ensate.gestetudiants.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableAsync
@SpringBootApplication
public class GestetudiantsApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(GestetudiantsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.findByNomUtilisateur("admin").isPresent()) {
            User admin = new User();
            admin.setNomUtilisateur("admin");
            admin.setMotDePasse(passwordEncoder.encode("admin")); // Encode the password
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created with username 'admin' and password 'admin'");
        }
    }

}