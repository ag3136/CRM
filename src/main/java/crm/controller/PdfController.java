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

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Controller for PDF generation.
 * Local FileOutputStream writes have been replaced with Amazon S3 PutObject calls
 * using AWS SDK for Java v2 to ensure durable, cloud-native storage.
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
     * Generates a PDF document and uploads it directly to Amazon S3.
     * Replaces the previous local FileOutputStream write (line 35) with an S3 PutObject call,
     * eliminating the ephemeral local file system dependency.
     *
     * @param fileName the desired object key name (without path prefix)
     * @param text     the paragraph text to embed in the PDF
     * @throws DocumentException if iText PDF generation fails
     * @throws IOException       if the in-memory stream cannot be written
     */
    private void generateAndUploadPdfToS3(String fileName, String text) throws DocumentException, IOException {
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

        // Resolve S3 bucket name: prefer injected property, fall back to environment variable
        String bucket = (s3BucketName != null && !s3BucketName.isEmpty())
                ? s3BucketName
                : System.getenv("AWS_S3_BUCKET_NAME");
        if (bucket == null || bucket.isEmpty()) {
            throw new IllegalStateException(
                    "S3 bucket name is not configured. " +
                    "Set the environment variable AWS_S3_BUCKET_NAME or the property aws.s3.bucket-name.");
        }

        String objectKey = s3PdfPrefix + fileName;

        // Upload the generated PDF bytes to S3 — replaces FileOutputStream(fileName)
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType("application/pdf")
                .build();
        s3Client.putObject(putRequest, RequestBody.fromBytes(baos.toByteArray()));

        log.info("PDF uploaded to S3: s3://{}/{}", bucket, objectKey);
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
                generateAndUploadPdfToS3(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
            } catch (DocumentException e) {
                log.error("PDF document generation failed for '{}': {}", pdf.getName(), e.getMessage());
            } catch (IOException e) {
                log.error("I/O error while generating PDF '{}': {}", pdf.getName(), e.getMessage());
            }
            return "pdf/success";
        }
    }
}
