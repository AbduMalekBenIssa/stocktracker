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
