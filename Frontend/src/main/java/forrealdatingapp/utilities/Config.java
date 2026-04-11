package forrealdatingapp.utilities;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties props = new Properties();

    static {
        // Use a static block to load the file when the class is first accessed
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("version.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find version.properties");
            } else {
                // This loads the file into the Properties object
                props.load(input);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}