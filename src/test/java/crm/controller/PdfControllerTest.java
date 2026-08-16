package crm.controller;

import crm.service.PdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private PdfController pdfController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pdfController).build();
    }

    @Test
    void pdfGenerator_shouldReturnPdfGeneratorView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/pdf-generator"))
                .andExpect(status().isOk())
                .andExpect(view().name("pdf/generator"))
                .andExpect(model().attributeExists("pdf"));
    }
}
