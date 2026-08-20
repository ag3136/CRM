package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.AzureBlobStorageService;
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

    private PdfService pdfService;
    private AzureBlobStorageService azureBlobStorageService;

    public PdfController(PdfService pdfService, AzureBlobStorageService azureBlobStorageService) {
        this.pdfService = pdfService;
        this.azureBlobStorageService = azureBlobStorageService;
    }

    /**
     * Generate PDF and upload to Azure Blob Storage instead of local file system.
     * This ensures data durability and availability across container restarts and scaling events.
     * 
     * @param fileName The name of the PDF file to generate
     * @param text The content to include in the PDF
     * @return The URL of the uploaded PDF in Azure Blob Storage
     * @throws DocumentException If PDF generation fails
     * @throws IOException If upload to Azure Blob Storage fails
     */
    private String generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Generate PDF in memory instead of writing to local file system
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();
            
            // Upload to Azure Blob Storage for persistent, cloud-native storage
            byte[] pdfBytes = outputStream.toByteArray();
            String blobUrl = azureBlobStorageService.uploadFile(fileName, pdfBytes);
            
            log.info("PDF generated and uploaded to Azure Blob Storage: {}", blobUrl);
            return blobUrl;
        } finally {
            outputStream.close();
        }
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
        } else {
            try {
                // Generate PDF and upload to Azure Blob Storage
                String blobUrl = generateSamplePdf(pdf.getName(), pdf.getContent());
                
                // Store the blob URL in the database for future reference
                pdf.setBlobUrl(blobUrl);
                pdfService.savePdf(pdf);
                
                log.info("PDF successfully generated and stored in Azure Blob Storage");
            } catch (DocumentException e) {
                log.error("Failed to generate PDF document: {}", e.getMessage(), e);
                return "redirect:/pdf-generator?error=document";
            } catch (IOException e) {
                log.error("Failed to upload PDF to Azure Blob Storage: {}", e.getMessage(), e);
                return "redirect:/pdf-generator?error=storage";
            } catch (Exception e) {
                log.error("Unexpected error during PDF generation: {}", e.getMessage(), e);
                return "redirect:/pdf-generator?error=unknown";
            }
            return "pdf/success";
        }
    }

}
