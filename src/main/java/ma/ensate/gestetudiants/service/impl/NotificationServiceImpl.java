// NotificationServiceImpl.java
package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendDemandeApprovedEmail(EtudiantBasicDTO etudiant, DemandeResponseDTO demande) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(etudiant.getEmail());
        message.setSubject("Votre demande a été approuvée");
        message.setText("Bonjour " + etudiant.getNom() + ",\n\n" +
                "Votre demande de (" + demande.getTypeDocument() + ") a été approuvée.\n\n" +
                "Cordialement,\nAdministration");
        mailSender.send(message);
    }

    @Override
    public void sendDemandeRejectedEmail(EtudiantBasicDTO etudiant, DemandeResponseDTO demande) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(etudiant.getEmail());
        message.setSubject("Votre demande a été rejetée");
        message.setText("Bonjour " + etudiant.getNom() + ",\n\n" +
                "Votre demande de (" + demande.getTypeDocument() + ") a été rejetée.\n\n" +
                "Cordialement,\nAdministration");
        mailSender.send(message);
    }

    @Override
    public void sendReclamationTreatedEmail(EtudiantBasicDTO etudiant, ReclamationResponseDTO reclamation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(etudiant.getEmail());
        message.setSubject("Votre réclamation a été traitée");
        message.setText("Bonjour " + etudiant.getNom() + ",\n\n" +
                "Votre réclamation concernant \"" + reclamation.getSujet() + "\" a été traitée.\n" +
                reclamation.getReponse() + "\n\n" +
                "Cordialement,\nAdministration");
        mailSender.send(message);
    }
}