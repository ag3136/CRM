package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Controller for PDF generation.
 * Replaces local FileOutputStream writes with Amazon S3 uploads
 * to ensure data durability in cloud/containerized environments.
 */
@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name:#{environment.AWS_S3_BUCKET_NAME}}")
    private String s3BucketName;

    @Value("${aws.s3.pdf-prefix:pdfs/}")
    private String s3PdfPrefix;

    public PdfController(PdfService pdfService, S3Client s3Client) {
        this.pdfService = pdfService;
        this.s3Client = s3Client;
    }

    /**
     * Generates a PDF in memory and uploads it to Amazon S3.
     * Replaces the previous local FileOutputStream write (blocker-2, line 35)
     * with an S3 PutObject call for durable, cloud-native storage.
     *
     * @param fileName the desired PDF file name
     * @param text     the text content to embed in the PDF
     * @throws DocumentException if iText PDF generation fails
     * @throws IOException       if byte stream I/O fails
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Generate PDF content into an in-memory byte array (no local file system dependency)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        // Upload the generated PDF bytes directly to Amazon S3
        String s3Key = s3PdfPrefix + fileName;
        byte[] pdfBytes = baos.toByteArray();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key(s3Key)
                .contentType("application/pdf")
                .contentLength((long) pdfBytes.length)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
        log.info("PDF '{}' successfully uploaded to S3 at s3://{}/{}", fileName, s3BucketName, s3Key);
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
                log.error("PDF document generation error: {}", e.getMessage());
            } catch (S3Exception e) {
                log.error("S3 upload error for PDF '{}': {}", pdf.getName(), e.getMessage());
            } catch (IOException e) {
                log.error("I/O error during PDF generation: {}", e.getMessage());
            }
            return "pdf/success";
        }
    }

}
