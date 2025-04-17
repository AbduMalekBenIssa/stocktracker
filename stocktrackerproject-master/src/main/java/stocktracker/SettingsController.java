import javafx.scene.control.*;
import stocktracker.BaseController;
import stocktracker.models.Settings;
import stocktracker.model.User;
import stocktracker.services.SettingsService;
import stocktracker.util.FileManager;

import java.io.IOException;

/**
 * Controller for the Settings view.
 * Manages user info and triggers data load/save.
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.3
 * @Tutorial T04
 */
public class SettingsController extends BaseController {
    @FXML
    private TextField userNameField;

    @FXML
    private Button resetButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadDataButton;

    @FXML
    private Button saveDataButton;

    private SettingsService settingsService;
    private Settings settings;
    private FileManager fileManager;

    @Override
    protected void onInitialize() {
        settingsService = SettingsService.getInstance();
        settings = settingsService.getSettings();
        fileManager = FileManager.getInstance();

        bindSettings();
    }
    private void bindSettings() {
        userNameField.textProperty().bindBidirectional(settings.userNameProperty());
    }

    @FXML
    private void handleSaveSettings() {
        try {
            if (user != null) {
                user.setName(settings.getUserName());
            }

            settingsService.saveSettings();

            showInfoDialog("Settings Saved", "Settings have been saved successfully.", "");

            if (mainController != null) {
                mainController.refreshUserInfo();
            }
        } catch (Exception e) {
            showErrorDialog("Error Saving Settings", "Could not save settings.", e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleReset() {
        settingsService.resetToDefaults();

        showInfoDialog("Settings Reset", "Settings have been reset to defaults.", "");

        if (mainController != null) {
            mainController.refreshUserInfo();
        }
    }

    @FXML
    private void handleLoadData() {
        try {
            User loadedUser = fileManager.loadUserData();
            this.user = loadedUser;
            if(mainController != null) {
                mainController.setUser(loadedUser);
                mainController.refreshUserInfo();
                mainController.loadDashboardView();
            }
            showInfoDialog("Data Loaded", "User data loaded successfully from " + fileManager.getDataFilePath(),
                    "Note: Some views might require reopening to fully reflect changes.");

        } catch (IOException e) {
            showErrorDialog("Error Loading Data", "Could not load user data from " + fileManager.getDataFilePath(), e.getMessage());
            e.printStackTrace();
        }

}
