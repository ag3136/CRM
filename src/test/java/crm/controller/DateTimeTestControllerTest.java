package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DateTimeTestControllerTest {

    @InjectMocks
    private DateTimeTestController dateTimeTestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dateTimeTestController).build();
    }

    @Test
    void dateTimeTest_shouldReturnDateTestView() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/date/test"))
                .andExpect(status().isOk())
                .andExpect(view().name("date/test"))
                .andExpect(model().attributeExists("standardDate"))
                .andExpect(model().attributeExists("localDateTime"))
                .andExpect(model().attributeExists("localDate"))
                .andExpect(model().attributeExists("timestamp"));
    }
}
