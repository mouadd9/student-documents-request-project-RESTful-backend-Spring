package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Reclamation;
import ma.ensate.gestetudiants.enums.TypeDocument;
import ma.ensate.gestetudiants.exception.EmailSendingException;
import ma.ensate.gestetudiants.service.NotificationService;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Override
    public CompletableFuture<Void> sendDemandeApprovedEmail(Demande demande, byte[] documentBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(demande.getEtudiant().getEmail());

            String subject;
            String body;
            String attachmentName;
            
            if (demande.getTypeDocument() == TypeDocument.ATTESTATION_SCOLARITE) {
                subject = "Votre demande d'attestation de scolarité a été approuvée";
                body = "Bonjour " + demande.getEtudiant().getNom()
                        + ",\n\nVotre demande d'attestation de scolarité a été approuvée.\nVeuillez trouver ci-joint le document demandé.\n\nCordialement,\nL'équipe Gestion Étudiants";
                attachmentName = "Attestation_Scolarite.pdf";
            } else if (demande.getTypeDocument() == TypeDocument.RELEVE_NOTES) {
                subject = "Votre demande de relevé de notes a été approuvée";
                body = "Bonjour " + demande.getEtudiant().getNom()
                        + ",\n\nVotre demande de relevé de notes a été approuvée.\nVeuillez trouver ci-joint le document demandé.\n\nCordialement,\nL'équipe Gestion Étudiants";
                attachmentName = "Releve_De_Notes.pdf";
            } else {
                throw new EmailSendingException("Type de document inconnu: " + demande.getTypeDocument());
            }

            helper.setSubject(subject);
            helper.setText(body);

            if (documentBytes != null && documentBytes.length > 0) {
                helper.addAttachment(attachmentName, new ByteArrayResource(documentBytes));
            }

            mailSender.send(message);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            logger.error("Failed to send approved demande email", e);
            throw new EmailSendingException("Échec de l'envoi de l'email de demande approuvé", e);
        } catch (Exception e) {
            logger.error("Failed to send approved demande email", e);
            throw new EmailSendingException("Échec de l'envoi de l'email de demande approuvé", e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Void> sendDemandeRejectedEmail(Demande demande) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(demande.getEtudiant().getEmail());
            message.setSubject("Demande Rejetée");
            message.setText("Bonjour " + demande.getEtudiant().getNom() + ",\n\n" +
                    "Votre demande de " + demande.getTypeDocument() + " a été rejetée.\n\n" +
                    "Cordialement,\nAdministration");
            mailSender.send(message);
            return CompletableFuture.completedFuture(null);
        } catch (MailException e) {
            logger.error("Failed to send rejected demande email", e);
            throw new EmailSendingException("Échec de l'envoi de l'email de demande rejetée.", e);
        }
    }

    @Override
    public CompletableFuture<Void> sendReclamationTreatedEmail(Reclamation reclamation) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reclamation.getEtudiant().getEmail());
            message.setSubject("Réclamation Traité");
            message.setText("Bonjour " + reclamation.getEtudiant().getNom() + ",\n\n" +
                    "Votre réclamation concernant \"" + reclamation.getSujet() + "\" a été traitée.\n" +
                    reclamation.getReponse() + "\n\n" +
                    "Cordialement,\nAdministration");
            mailSender.send(message);
            return CompletableFuture.completedFuture(null);
        } catch (MailException e) {
            logger.error("Failed to send treated reclamation email", e);
            throw new EmailSendingException("Échec de l'envoi de l'email de réclamation traitée.", e);
        }
    }
}