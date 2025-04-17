package stocktracker;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.application.Platform;

import stocktracker.BaseController;
import stocktracker.MainViewController;
import stocktracker.model.User;
import stocktracker.service.StockMarket;
import stocktracker.model.*;
import stocktracker.model.Stock;
import stocktracker.model.WatchlistStock;
import stocktracker.model.OwnedStock;
import stocktracker.model.BuyTransaction;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class StockDetailController extends BaseController {

    @FXML
    private Label symbolLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label changeLabel;

    @FXML
    private AreaChart<Number, Number> priceChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label marketCapLabel;

    @FXML
    private Label peRatioLabel;

    @FXML
    private Label dividendYieldLabel;

    @FXML
    private Label volumeLabel;

    @FXML
    private TextField sharesTextField;

    @FXML
    private Button buyButton;

    @FXML
    private Button addToWatchlistButton;

    @FXML
    private VBox newsContainer;

    private Stock currentStock;
    private StringProperty stockSymbol = new SimpleStringProperty();

    @Override
    protected void onInitialize() {
        System.out.println("StockDetailController initialized with symbol: " + stockSymbol.get());

        // Bind the stock symbol property to update the view when it changes
        stockSymbol.addListener((observable, oldValue, newValue) -> {
            System.out.println("Stock symbol changed from: " + oldValue + " to: " + newValue);
            if (newValue != null && !newValue.isEmpty()) {
                loadStockData(newValue);
            }
        });

        // Set up chart
        setupChart();

        // If stock symbol was set before initialization, load it now
        if (stockSymbol.get() != null && !stockSymbol.get().isEmpty()) {
            System.out.println("Loading initial stock data for symbol: " + stockSymbol.get());
            loadStockData(stockSymbol.get());
        } else {
            System.out.println("No stock symbol set during initialization");
        }
    }

    private void setupChart() {
        // Configure axis
        xAxis.setLabel("Time");
        yAxis.setLabel("Price");

        // Remove legend
        priceChart.setLegendVisible(false);

        // Make chart look cleaner
        priceChart.setCreateSymbols(false);
        priceChart.setAnimated(false);
    }
    public void setStockSymbol(String symbol) {
        System.out.println("Setting stock symbol to: " + symbol);
        this.stockSymbol.set(symbol);

        // Force data load if controller is already initialized
        if (stockMarket != null) {
            System.out.println("Controller already initialized, loading data immediately");
            loadStockData(symbol);
        } else {
            System.out.println("Controller not fully initialized yet, will load data later");
        }
    }