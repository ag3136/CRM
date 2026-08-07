package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.config.AzureBlobStorageService;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final AzureBlobStorageService azureBlobStorageService;

    public PdfController(PdfService pdfService, AzureBlobStorageService azureBlobStorageService) {
        this.pdfService = pdfService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    private String generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        String normalizedFileName = fileName.endsWith(".pdf") ? fileName : fileName + ".pdf";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(new Paragraph(text));
        document.close();
        String blobName = azureBlobStorageService.createBlobName("pdf", normalizedFileName);
        return azureBlobStorageService.uploadStream(blobName,
                new java.io.ByteArrayInputStream(outputStream.toByteArray()), outputStream.size());
    }

    @GetMapping("/pdf-generator")
    public String pdfGenerator(Model model) {
        model.addAttribute("pdf", new Pdf());
        return "pdf/generator";
    }

    @PostMapping("/pdf-generator")
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/pdf-generator";
        }
        try {
            String blobUrl = generateSamplePdf(pdf.getName(), pdf.getContent());
            log.info("Generated PDF uploaded to Azure Blob Storage at {}", blobUrl);
            pdfService.savePdf(pdf);
        } catch (DocumentException e) {
            log.info("Unable to generate PDF document", e);
        } catch (IOException e) {
            log.info("Unable to upload PDF to Azure Blob Storage", e);
        }
        return "pdf/success";
    }
}
