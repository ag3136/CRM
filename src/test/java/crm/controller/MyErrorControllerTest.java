package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.RequestDispatcher;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MyErrorControllerTest {

    @InjectMocks
    private MyErrorController errorController;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    @Test
    void handleError_with404Status_shouldReturnError404View() {
        // Arrange
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);

        // Act
        String result = errorController.handleError(request);

        // Assert
        assertEquals("error-404", result);
    }

    @Test
    void handleError_with403Status_shouldReturn403View() {
        // Arrange
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 403);

        // Act
        String result = errorController.handleError(request);

        // Assert
        assertEquals("403", result);
    }

    @Test
    void handleError_with500Status_shouldReturnError500View() {
        // Arrange
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);

        // Act
        String result = errorController.handleError(request);

        // Assert
        assertEquals("error-500", result);
    }

    @Test
    void handleError_withNoStatus_shouldReturnDefaultErrorView() {
        // Act
        String result = errorController.handleError(request);

        // Assert
        assertEquals("error", result);
    }

    @Test
    void handleError_withUnknownStatus_shouldReturnDefaultErrorView() {
        // Arrange
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 999);

        // Act
        String result = errorController.handleError(request);

        // Assert
        assertEquals("error", result);
    }

    @Test
    void myErrorController_shouldImplementErrorController() {
        assertTrue(org.springframework.boot.web.servlet.error.ErrorController.class
                .isAssignableFrom(MyErrorController.class));
    }
}
