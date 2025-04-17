package stocktracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import stocktracker.model.User;
import stocktracker.service.FinancialModelPrepAPI;
import stocktracker.service.StockMarket;
import stocktracker.util.FileManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Main class for the Stock Tracker JavaFX Application
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class StockTrackerGUI extends Application {

    private static User user;
    private static StockMarket stockMarket;
    private static String initialDataFile;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main window FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/stocktracker/views/MainView.fxml"));
        Parent root = loader.load();

        // Set the main controller and pass the user and stock market service
        MainViewController controller = loader.getController();
        controller.initialize(user, stockMarket);

        // Setup the primary stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/stocktracker/css/style.css").toExternalForm());

        primaryStage.setTitle("Stock Tracker");
        primaryStage.setScene(scene);

        // Try to load application icon if available
        try {
            InputStream iconStream = getClass().getResourceAsStream("/stocktracker/images/app_icon.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            }
        } catch (Exception e) {
            System.out.println("Could not load application icon: " + e.getMessage());
        }

        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }


    /**
     * Main method to launch the application
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Initialize stock market service
        stockMarket = new FinancialModelPrepAPI();

        // Always start with a fresh user by default, instead of loading automatically
        createNewUser();
        System.out.println("Starting with a new user. Use Settings > Load Data to restore previous data.");

        // Launch the JavaFX application
        launch(args);
    }
