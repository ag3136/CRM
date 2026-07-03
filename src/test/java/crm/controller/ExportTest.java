package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private Export export;

    @Test
    void testDownload_returnsEmptyString() {
        when(userService.listAllUsers()).thenReturn(Arrays.asList());
        String view = export.download(model);
        assertEquals("", view);
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testDownload_callsListAllUsers() {
        when(userService.listAllUsers()).thenReturn(Arrays.asList());
        export.download(model);
        verify(userService).listAllUsers();
    }

    @Test
    void testConstructor_withService_createsInstance() {
        Export exportController = new Export(userService);
        assertNotNull(exportController);
    }
}
