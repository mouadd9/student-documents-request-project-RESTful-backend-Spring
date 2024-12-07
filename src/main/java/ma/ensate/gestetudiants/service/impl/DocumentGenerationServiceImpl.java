package ma.ensate.gestetudiants.service.impl;

import ma.ensate.gestetudiants.entity.Etudiant;
import ma.ensate.gestetudiants.exception.ResourceNotFoundException;
import ma.ensate.gestetudiants.repository.EtudiantRepository;
import ma.ensate.gestetudiants.service.DocumentGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public byte[] generateAttestation(Long etudiantId) throws Exception {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec l'id " + etudiantId));

        Context context = new Context();
        context.setVariable("etudiant", etudiant);
        context.setVariable("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        String htmlContent = templateEngine.process("attestation", context);
        return convertHtmlToPdf(htmlContent);
    }

    @Override
    public byte[] generateReleveDeNotes(Long etudiantId) throws Exception {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé avec l'id " + etudiantId));

        Context context = new Context();
        context.setVariable("etudiant", etudiant);
        context.setVariable("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        String htmlContent = templateEngine.process("releve_de_notes", context);
        return convertHtmlToPdf(htmlContent);
    }

    private byte[] convertHtmlToPdf(String htmlContent) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // Dynamically set the base URL to locate resources in the static/ directory
            URL resource = getClass().getClassLoader().getResource("static/");
            String baseUrl = resource != null ? resource.toString() : "";
            renderer.setDocumentFromString(htmlContent, baseUrl);

            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }
}