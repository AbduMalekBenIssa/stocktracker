package stocktracker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    @FXML
    private Button aboutButton;

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

    /**
     * Shows stock details for a specific symbol
     *
     * @param symbol The stock symbol to display
     */
    public void showStockDetails(String symbol) {
        try {
            System.out.println("Showing stock details for: " + symbol);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stocktracker/views/StockDetailView.fxml"));
            Parent viewNode = loader.load();

            StockDetailController controller = loader.getController();

            // Initialize the controller with dependencies first
            controller.initialize(user, stockMarket, this);

            // Then set the stock symbol to trigger data loading
            controller.setStockSymbol(symbol);

            // Clear existing content and add the new view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(viewNode);

        } catch (IOException e) {
            showErrorDialog("Error Loading View", "Could not load stock details: " + symbol, e.getMessage());
        }
    }

    /**
     * Loads a view into the content area
     *
     * @param fxmlPath The path to the FXML file
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent viewNode = loader.load();

            // If the controller is a BaseController, initialize it
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).initialize(user, stockMarket, this);
            }

            // Clear existing content and add the new view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(viewNode);

        } catch (IOException e) {
            showErrorDialog("Error Loading View", "Could not load view: " + fxmlPath, e.getMessage());
        }
    }

    /**
     * Shows an error dialog
     *
     * @param title The dialog title
     * @param header The dialog header
     * @param content The dialog content
     */
    public void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Updates the displayed user information after changes
     */
    public void refreshUserInfo() {
        updateUserInfo();
    }

    /**
     * Gets the main Scene associated with this controller's view.
     *
     * @return The main Scene, or null if the view hasn't been added to a scene yet.
     */
    public Scene getScene() {
        return mainBorderPane != null ? mainBorderPane.getScene() : null;
    }

    /**
     * Handles the action of the 'About' button in the sidebar.
     * Displays an information dialog with application details, styled to match the theme.
     */
    @FXML
    private void handleAboutAction() {
        // Create a custom Dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("About StockSnap");
        dialog.setHeaderText("StockSnap - Portfolio Management System");

        // Create content VBox
        VBox contentVBox = new VBox(10);
        contentVBox.setPadding(new Insets(20));

        // Add content Labels
        Label developedByLabel = new Label("Developed by: Omar Almishri, AbduMalek Ben Issa");
        Label tutorialLabel = new Label("Tutorial: T04");
        Label descriptionLabel = new Label("This application helps you track your stock portfolio, watchlist, and market trends.");
        descriptionLabel.setWrapText(true);
        Label contactLabel = new Label("Contact: omar.almishri56@gmail.com, abdumalek.benissa@gmail.com");

        contentVBox.getChildren().addAll(
                developedByLabel,
                tutorialLabel,
                new Separator(),
                descriptionLabel,
                new Separator(),
                contactLabel
        );

        // Get the DialogPane
        DialogPane dialogPane = dialog.getDialogPane();

        // Apply the application's stylesheet to the dialog
        try {
            String cssPath = getClass().getResource("/stocktracker/css/style.css").toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            // Add a style class for specific styling if needed
            dialogPane.getStyleClass().add("about-dialog-pane");
        } catch (NullPointerException e) {
            System.err.println("Could not load stylesheet for About dialog: /stocktracker/css/style.css not found.");
        }

        // Set content
        dialogPane.setContent(contentVBox);

        // Add OK button
        dialogPane.getButtonTypes().add(ButtonType.OK);

        // Show dialog
        dialog.showAndWait();
    }
}