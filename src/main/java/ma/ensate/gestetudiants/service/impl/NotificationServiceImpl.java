// NotificationServiceImpl.java
package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.dto.demande.DemandeResponseDTO;
import ma.ensate.gestetudiants.dto.etudiant.EtudiantBasicDTO;
import ma.ensate.gestetudiants.dto.reclamation.ReclamationResponseDTO;
import ma.ensate.gestetudiants.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendDemandeApprovedEmail(EtudiantBasicDTO etudiant, DemandeResponseDTO demande, byte[] documentBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(etudiant.getEmail());

            String subject;
            String body;
            String attachmentName;

            switch (demande.getTypeDocument()) {
                case ATTESTATION_SCOLARITE:
                    subject = "Votre Attestation de Scolarité a été approuvée";
                    body = "Bonjour " + etudiant.getNom() + ",\n\n" +
                            "Votre demande d'Attestation de Scolarité a été approuvée.\n\n" +
                            "Veuillez trouver votre attestation en pièce jointe.\n\n" +
                            "Cordialement,\nAdministration";
                    attachmentName = "Attestation_Scolarite_" + etudiant.getNom() + ".pdf";
                    break;
                case RELEVE_NOTES:
                    subject = "Votre Relevé de Notes a été approuvé";
                    body = "Bonjour " + etudiant.getNom() + ",\n\n" +
                            "Votre demande de Relevé de Notes a été approuvée.\n\n" +
                            "Veuillez trouver votre relevé de notes en pièce jointe.\n\n" +
                            "Cordialement,\nAdministration";
                    attachmentName = "Releve_de_Notes_" + etudiant.getNom() + ".pdf";
                    break;
                default:
                    throw new IllegalArgumentException("Type de document inconnu: " + demande.getTypeDocument());
            }

            helper.setSubject(subject);
            helper.setText(body);

            helper.addAttachment(attachmentName, new ByteArrayResource(documentBytes));

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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