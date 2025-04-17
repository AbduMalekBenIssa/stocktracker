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

    /**
     * Initializes the controller with user and stock market service
     *
     * @param user The user
     * @param stockMarket The stock market service
     */
    public void initialize(User user, StockMarket stockMarket) {
        this.user = user;
        this.stockMarket = stockMarket;

        // Update header information
        updateUserInfo();

        // Load the dashboard as the initial view
        loadDashboardView();
    }

    /**
     * Updates the user information displayed in the header
     */
    private void updateUserInfo() {
        if (user != null) {
            userNameLabel.setText(user.getName());
            balanceLabel.setText(String.format("$%.2f", user.getBalance()));
            totalValueLabel.setText(String.format("$%.2f", user.getTotalValue()));
        }

        /**
         * Loads the dashboard view
         */
        @FXML
        public void loadDashboardView() {
            loadView("/stocktracker/views/DashboardView.fxml");
        }

        /**
         * Loads the portfolio view
         */
        @FXML
        private void loadPortfolioView() {
            loadView("/stocktracker/views/PortfolioView.fxml");
        }

        /**
         * Loads the watchlist view
         */
        @FXML
        private void loadWatchlistView() {
            loadView("/stocktracker/views/WatchlistView.fxml");
        }

        /**
         * Loads the transaction view
         */
        @FXML
        private void loadTransactionView() {
            loadView("/stocktracker/views/TransactionView.fxml");
        }

        /**
         * Loads the market view
         */
        @FXML
        private void loadMarketView() {
            loadView("/stocktracker/views/MarketView.fxml");
        }

        /**
         * Loads the settings view
         */
        @FXML
        private void loadSettingsView() {
            loadView("/stocktracker/views/SettingsView.fxml");
        }


    }
}
