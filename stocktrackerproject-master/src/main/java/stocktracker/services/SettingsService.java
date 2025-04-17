
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

    /**
     * Gets the singleton instance of the SettingsService.
     *
     * @return The singleton instance.
     */
    public static synchronized SettingsService getInstance() {
        if (instance == null) {
            instance = new SettingsService();
        }
        return instance;
    }

    /**
     * Gets the current Settings object.
     *
     * @return The Settings object containing current application settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Loads settings from the properties file into the Settings object.
     * If the file doesn't exist or an error occurs, it sets default values.
     */
    public void loadSettings() {
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("SettingsService: Error loading properties file: " + e.getMessage());
            setSettingsToDefaults();
            return;
        }

        settings.setUserName(properties.getProperty(KEY_USERNAME, DEFAULT_USERNAME));
        settings.setCurrency(properties.getProperty(KEY_CURRENCY, DEFAULT_CURRENCY));
    }

    /**
     * Saves the current settings from the Settings object to the properties file.
     */
    public void saveSettings() {
        properties.setProperty(KEY_USERNAME, settings.getUserName());
        properties.setProperty(KEY_CURRENCY, settings.getCurrency());

        try (FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE)) {
            properties.store(fos, "Stock Tracker Application Settings");
        } catch (IOException e) {
            System.err.println("SettingsService: Error saving properties file: " + e.getMessage());
        }
    }

    /**
     * Resets all settings in the Settings object to their default values.
     * Note: This does NOT automatically save the reset settings to the file.
     * Call saveSettings() afterwards if persistence is desired.
     */
    public void resetToDefaults() {
        setSettingsToDefaults();
    }

    /**
     * Helper method to apply default values to the Settings object.
     */
    private void setSettingsToDefaults() {
        settings.setUserName(DEFAULT_USERNAME);
        settings.setCurrency(DEFAULT_CURRENCY);
    }
