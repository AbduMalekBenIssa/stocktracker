package stocktracker;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stocktracker.models.Settings;
import stocktracker.model.User;
import stocktracker.services.SettingsService;
import stocktracker.util.FileManager;

import java.io.File;
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

    @FXML
    private Label dataNoticeLabel;

    private SettingsService settingsService;
    private Settings settings;
    private FileManager fileManager;

    @Override
    protected void onInitialize() {
        settingsService = SettingsService.getInstance();
        settings = settingsService.getSettings();
        fileManager = FileManager.getInstance();

        bindSettings();

        // Update the notice label to inform users about loading saved data
        if (dataNoticeLabel != null) {
            dataNoticeLabel.setText("The application starts with a new user by default. Click 'Load Data' to restore your previously saved data.");
            dataNoticeLabel.getStyleClass().add("info-text");
        }
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load User Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        // Set initial directory to the current data file path if it exists
        String currentPath = fileManager.getDataFilePath();
        File initialDir = new File(currentPath);
        if (initialDir.getParentFile() != null && initialDir.getParentFile().exists()) {
            fileChooser.setInitialDirectory(initialDir.getParentFile());
        }

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(loadDataButton.getScene().getWindow());
        if (selectedFile == null) {
            // User canceled
            return;
        }

        try {
            // Temporarily set the file path to the selected file
            String originalPath = fileManager.getDataFilePath();
            fileManager.setDataFilePath(selectedFile.getAbsolutePath());

            try {
                User loadedUser = fileManager.loadUserData();
                this.user = loadedUser;
                if(mainController != null) {
                    mainController.setUser(loadedUser);
                    mainController.refreshUserInfo();
                    mainController.loadDashboardView();
                }
                showInfoDialog("Data Loaded", "User data loaded successfully from " + selectedFile.getAbsolutePath(),
                        "Note: Some views might require reopening to fully reflect changes.");
            } finally {
                // Restore the original path unless user wants to keep this as default
                if (showConfirmationDialog("Set as Default?",
                        "Would you like to use this location as the default for future operations?",
                        "")) {
                    // User wants this as new default, save it to configuration
                    fileManager.saveDataConfiguration();
                } else {
                    fileManager.setDataFilePath(originalPath);
                }
            }
        } catch (IOException e) {
            showErrorDialog("Error Loading Data", "Could not load user data from the selected file", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveData() {
        if (this.user == null) {
            showErrorDialog("Error Saving Data", "Cannot save data.", "No user data available to save.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save User Data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        // Set default file name
        fileChooser.setInitialFileName("userdata.txt");

        // Set initial directory to the current data file path if it exists
        String currentPath = fileManager.getDataFilePath();
        File initialDir = new File(currentPath);
        if (initialDir.getParentFile() != null && initialDir.getParentFile().exists()) {
            fileChooser.setInitialDirectory(initialDir.getParentFile());
        }

        // Show the file chooser dialog
        File selectedFile = fileChooser.showSaveDialog(saveDataButton.getScene().getWindow());
        if (selectedFile == null) {
            // User canceled
            return;
        }

        try {
            // Temporarily set the file path to the selected file
            String originalPath = fileManager.getDataFilePath();
            fileManager.setDataFilePath(selectedFile.getAbsolutePath());

            try {
                fileManager.saveUserData(this.user);
                showInfoDialog("Data Saved", "User data saved successfully to " + selectedFile.getAbsolutePath(), "");
            } finally {
                // Restore the original path unless user wants to keep this as default
                if (showConfirmationDialog("Set as Default?",
                        "Would you like to use this location as the default for future operations?",
                        "")) {
                    // User wants this as new default, save it to configuration
                    fileManager.saveDataConfiguration();
                } else {
                    fileManager.setDataFilePath(originalPath);
                }
            }
        } catch (IOException e) {
            showErrorDialog("Error Saving Data", "Could not save user data to the selected file", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows a confirmation dialog and returns the user's choice
     * @return true if the user confirmed, false otherwise
     */
    private boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
} 