package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.entity.Reclamation;
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
    public void sendDemandeApprovedEmail(Etudiant etudiant, Demande demande) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(etudiant.getEmail());
        message.setSubject("Votre demande a été approuvée");
        message.setText("Bonjour " + etudiant.getNom() + ",\n\nVotre demande de ( " + demande.getTypeDocument() + " ) a été approuvée.\n\nCordialement,\nAdministration");
        mailSender.send(message);
    }

    @Override
    public void sendDemandeRejectedEmail(Etudiant etudiant, Demande demande) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(etudiant.getEmail());
        message.setSubject("Votre demande a été rejetée");
        message.setText("Bonjour " + etudiant.getNom() + ",\n\nVotre demande de ( " + demande.getTypeDocument() + " ) a été rejetée.\n\nCordialement,\nAdministration");
        mailSender.send(message);
    }

    @Override
    public void sendReclamationTreatedEmail(Etudiant etudiant, Reclamation reclamation) {
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