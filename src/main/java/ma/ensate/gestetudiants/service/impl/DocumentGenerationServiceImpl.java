package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.entity.Demande;
import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.repository.DemandeRepository;
import ma.ensate.gestetudiants.service.DocumentGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public byte[] generateDocument(Long demandeId) throws Exception {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'id " + demandeId));

        Etudiant etudiant = demande.getEtudiant();

        // Préparer le contexte Thymeleaf
        Context context = new Context();
        context.setVariable("etudiant", etudiant);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        context.setVariable("dateActuelle", sdf.format(new Date()));

        // Définir le baseUrl pour les ressources
        URL resource = getClass().getClassLoader().getResource("static/");
        String baseUrl = resource != null ? resource.toString() : "";
        context.setVariable("baseUrl", baseUrl);

        // Générer le contenu HTML
        String htmlContent = templateEngine.process("attestation", context);

        // Convertir le HTML en PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent, baseUrl);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }
}