package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.GoogleCloudStorageService;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;
    private GoogleCloudStorageService gcsService;

    @Value("${gcs.pdf.folder:pdfs}")
    private String gcsPdfFolder;

    public PdfController(PdfService pdfService, GoogleCloudStorageService gcsService) {
        this.pdfService = pdfService;
        this.gcsService = gcsService;
    }

    /**
     * Generate a PDF and upload it to Google Cloud Storage instead of local file system.
     * This ensures data persistence across container restarts and scaling events.
     *
     * @param fileName Name of the PDF file
     * @param text     Content to include in the PDF
     * @return The GCS URL of the uploaded PDF
     */
    private String generateAndUploadPdf(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Create PDF in memory using ByteArrayOutputStream instead of FileOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            // Upload the PDF to Google Cloud Storage
            String gcsFileName = gcsPdfFolder + "/" + fileName;
            String gcsUrl = gcsService.uploadFile(gcsFileName, outputStream.toByteArray(), "application/pdf");
            
            log.info("PDF generated and uploaded to GCS: {}", gcsUrl);
            return gcsUrl;
        } catch (Exception e) {
            log.error("Failed to generate or upload PDF: {}", fileName, e);
            throw new DocumentException("Failed to generate or upload PDF", e);
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
                String gcsUrl = generateAndUploadPdf(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
                
                // Add the GCS URL to the model so it can be displayed to the user
                model.addAttribute("gcsUrl", gcsUrl);
                model.addAttribute("message", "PDF generated and uploaded successfully to Google Cloud Storage");
                
                log.info("PDF '{}' generated and saved successfully", pdf.getName());
            } catch (DocumentException e) {
                log.error("Failed to generate PDF document", e);
                model.addAttribute("error", "Failed to generate PDF document");
                return "pdf/generator";
            } catch (Exception e) {
                log.error("Failed to upload PDF to Google Cloud Storage", e);
                model.addAttribute("error", "Failed to upload PDF to cloud storage");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }

}
