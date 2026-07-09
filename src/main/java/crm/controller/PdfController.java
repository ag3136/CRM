package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;

/**
 * Controller for PDF generation.
 *
 * <p>The generated PDF is uploaded directly to Amazon S3 instead of being
 * written to the local file system.  This ensures data durability and
 * availability in containerised / serverless environments where the local
 * file system is ephemeral (12-factor app principle VI – Processes).
 *
 * <p>Required environment variables:
 * <ul>
 *   <li>{@code S3_BUCKET_NAME} – name of the target S3 bucket</li>
 *   <li>{@code AWS_REGION}     – AWS region (used by the default SDK credential chain)</li>
 * </ul>
 */
@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final S3Client s3Client;

    public PdfController(PdfService pdfService, S3Client s3Client) {
        this.pdfService = pdfService;
        this.s3Client = s3Client;
    }

    /**
     * Generates a PDF in memory and uploads it to Amazon S3.
     *
     * @param fileName the desired S3 object key / file name
     * @param text     the text content to embed in the PDF
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Build the PDF entirely in memory – no local file system dependency.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        // Upload the in-memory PDF bytes to Amazon S3.
        String bucketName = System.getenv("S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable S3_BUCKET_NAME is not set. Cannot upload PDF to S3.");
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(baos.toByteArray()));
        log.info("PDF '{}' successfully uploaded to S3 bucket '{}'.", fileName, bucketName);
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
                generateSamplePdf(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
            } catch (DocumentException e) {
                log.error("Failed to generate PDF document: {}", e.getMessage());
            } catch (IllegalStateException e) {
                log.error("S3 configuration error: {}", e.getMessage());
            }
            return "pdf/success";
        }
    }
}
