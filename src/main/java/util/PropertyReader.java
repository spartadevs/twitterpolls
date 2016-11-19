package util;

import com.google.common.io.Resources;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static String filePath = "producer.props";

    public static Properties get() {
        try (InputStream props = Resources.getResource(filePath).openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            return properties;
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return null;
    }
}
