
import stocktracker.models.Settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Service class for managing application settings.
 * Implements the Singleton pattern.
 */
public class SettingsService {

    private static final String PROPERTIES_FILE = "stocktracker.properties";
    private static SettingsService instance;
    private final Settings settings;
    private final Properties properties;

    // Property Keys
    private static final String KEY_USERNAME = "user.name";
    private static final String KEY_CURRENCY = "data.currency";

    // Default Values
    private static final String DEFAULT_USERNAME = "User";
    private static final String DEFAULT_CURRENCY = "USD ($)";

    private SettingsService() {
        settings = new Settings();
        properties = new Properties();
        loadSettings();
    }
