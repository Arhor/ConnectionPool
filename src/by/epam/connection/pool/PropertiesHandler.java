package by.epam.connection.pool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public abstract class PropertiesHandler {
    
    private static final Logger LOG = LogManager.getLogger();
    private static final String PROPERTY_PATH = "resources/connectionpool.properties";

    public static Properties readProperties() {
        try (FileInputStream fis = new FileInputStream(PROPERTY_PATH)) {
            Properties prop = new Properties();
            prop.load(fis);
            return prop;
        } catch (IOException e) {
            LOG.error("I/O exception occured: ", e);
            throw new RuntimeException();
        }
    }
}
