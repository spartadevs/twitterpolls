package util;

import com.google.common.io.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static Properties properties = null;

    private static void loadAllPropertiesInFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                loadAllPropertiesInFolder(fileEntry);
            } else {
                loadPropertiesFromFile(fileEntry.getAbsolutePath());
            }
        }
    }

    private static void loadPropertiesFromFile(String filePath) {
        if (properties == null) {
            properties = new Properties();
        }

        try (InputStream props = new FileInputStream(filePath)) {
            properties.load(props);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public static Properties get() {
        if (properties == null) {
            loadAllPropertiesInFolder(new File("src/main/resources/props"));
        }

        return properties;
    }
}
