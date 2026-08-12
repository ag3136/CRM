package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import crm.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;
    private S3StorageService s3StorageService;

    public PdfController(PdfService pdfService, S3StorageService s3StorageService) {
        this.pdfService = pdfService;
        this.s3StorageService = s3StorageService;
    }

    /**
     * Generate PDF and upload to S3 instead of writing to local file system.
     * This ensures data durability and availability in cloud environments.
     *
     * @param fileName The name of the PDF file
     * @param text The content to include in the PDF
     * @return The S3 key where the file was uploaded
     */
    private String generateAndUploadPdfToS3(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Generate PDF in memory using ByteArrayOutputStream instead of FileOutputStream
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();
            
            // Generate S3 key with timestamp to ensure uniqueness
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            String s3Key = String.format("pdfs/%s-%s", timestamp, fileName);
            
            // Upload to S3
            byte[] pdfBytes = outputStream.toByteArray();
            s3StorageService.uploadFile(s3Key, pdfBytes, "application/pdf");
            
            log.info("PDF generated and uploaded to S3: {}", s3StorageService.getS3Uri(s3Key));
            return s3Key;
            
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
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/pdf-generator";
        } else {
            try {
                // Generate PDF and upload to S3 instead of local file system
                String s3Key = generateAndUploadPdfToS3(pdf.getName(), pdf.getContent());
                
                // Store S3 reference in database
                pdfService.savePdf(pdf);
                
                // Add S3 location to model for display
                model.addAttribute("s3Location", s3StorageService.getS3Uri(s3Key));
                log.info("PDF successfully generated and stored in S3 for: {}", pdf.getName());
                
            } catch (DocumentException e) {
                log.error("Error generating PDF document", e);
                model.addAttribute("error", "Failed to generate PDF document");
                return "pdf/generator";
            } catch (IOException e) {
                log.error("Error uploading PDF to S3", e);
                model.addAttribute("error", "Failed to upload PDF to cloud storage");
                return "pdf/generator";
            } catch (Exception e) {
                log.error("Unexpected error during PDF generation", e);
                model.addAttribute("error", "An unexpected error occurred");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }

}
