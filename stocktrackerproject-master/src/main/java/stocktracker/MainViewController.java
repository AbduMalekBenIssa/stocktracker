package stocktracker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import stocktracker.model.User;
import stocktracker.service.StockMarket;

import java.io.IOException;

/**
 * Controller for the main view of the Stock Tracker application
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class MainViewController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private StackPane contentArea;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label totalValueLabel;

    private User user;
    private StockMarket stockMarket;

    /**
     * Sets the current user for the application.
     * Should be called when loading data.
     *
     * @param user The new User object.
     */
    public void setUser(User user) {
        this.user = user;
        // Optionally immediately refresh UI elements dependent on user
        // refreshUserInfo(); // This will be called separately in SettingsController
    }
}
