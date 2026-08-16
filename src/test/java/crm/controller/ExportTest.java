package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private Export export;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(export).build();
    }

    @Test
    void download_shouldReturnEmptyViewName() throws Exception {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/download"))
                .andExpect(status().isOk())
                .andExpect(view().name(""))
                .andExpect(model().attributeExists("users"));
        
        verify(userService).listAllUsers();
    }
}
