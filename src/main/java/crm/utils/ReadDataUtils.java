package crm.utils;

import java.io.File;

public class ReadDataUtils {

    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        // Swing-based file chooser is not available in headless server environments.
        // This utility method is intended for desktop/standalone use only.
        throw new UnsupportedOperationException(
                "ReadDataUtils.ReadFile is not supported in a server environment. " +
                "Use a file upload mechanism instead.");
    }

}
