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
    private void loadStockData(String symbol) {
        System.out.println("*** loadStockData called for symbol: " + symbol);
        try {
            if (stockMarket == null) {
                System.out.println("*** ERROR: stockMarket is null!");
                return;
            }

            // Create a stock object with data from the stock market service
            System.out.println("*** Getting company name for: " + symbol);
            String name = stockMarket.getCompanyName(symbol);
            System.out.println("*** Company name: " + name);

            System.out.println("*** Getting stock price for: " + symbol);
            double price = stockMarket.getStockPrice(symbol);
            System.out.println("*** Stock price: " + price);

            System.out.println("*** Getting daily change percentage for: " + symbol);
            double changePercent = stockMarket.getDailyChangePercentage(symbol);
            System.out.println("*** Change percentage: " + changePercent);

            // Calculate the change amount based on the percentage
            double changeAmount = price * changePercent / 100;
            System.out.println("*** Change amount: " + changeAmount);

            // Create a stock object to store the data
            Stock stock = new Stock(symbol, name, price) {
                @Override
                public int compareTo(Stock o) {
                    return this.getSymbol().compareTo(o.getSymbol());
                }
            };
            stock.setChange(changeAmount);
            stock.setChangePercent(changePercent);

            // Set mock values for other properties
            stock.setMarketCap(price * 1000000000); // Mock market cap
            stock.setPeRatio(30 + (Math.random() * 10)); // Mock P/E ratio
            stock.setDividendYield(Math.random() * 3); // Mock dividend yield
            stock.setVolume((int)(Math.random() * 10000000)); // Mock volume
            // Store the stock
            currentStock = stock;

            if (currentStock != null) {
                System.out.println("*** Updating UI with stock info");
                // Update the labels with stock information
                updateStockInfo();

                // Load price history
                loadPriceHistory();

                // Load news items
                loadNewsItems();

                // Update watchlist button state
                updateWatchlistButtonState();
            }
        } catch (Exception e) {
            // Handle exceptions (show error message, log, etc.)
            System.out.println("*** ERROR loading stock data: " + e.getMessage());
            e.printStackTrace();
            showErrorDialog("Error Loading Stock", "Failed to load stock data", e.getMessage());
        }
    }
    private void updateStockInfo() {
        System.out.println("*** updateStockInfo called");

        // Use Platform.runLater to update UI components on the JavaFX application thread
        Platform.runLater(() -> {
            try {
                // Check if UI components are initialized
                if (symbolLabel == null || nameLabel == null || priceLabel == null || changeLabel == null) {
                    System.out.println("*** ERROR: UI components are not initialized!");
                    return;
                }
                // Format currency values
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                percentFormat.setMaximumFractionDigits(2);

                // Update header information
                System.out.println("*** Setting symbol label to: " + currentStock.getSymbol());
                symbolLabel.setText(currentStock.getSymbol());

                System.out.println("*** Setting name label to: " + currentStock.getName());
                nameLabel.setText(currentStock.getName());

                System.out.println("*** Setting price label to: " + currencyFormat.format(currentStock.getPrice()));
                priceLabel.setText(currencyFormat.format(currentStock.getPrice()));

                // Set change amount and percentage with appropriate styling
                double changeAmount = currentStock.getChange();
                double changePercent = currentStock.getChangePercent();

                String changeText = String.format("%+.2f (%+.2f%%)",
                        changeAmount, changePercent);
                System.out.println("*** Setting change label to: " + changeText);
                changeLabel.setText(changeText);

                // Apply CSS style based on whether the change is positive or negative
                if (changeAmount >= 0) {
                    changeLabel.getStyleClass().remove("negative");
                    changeLabel.getStyleClass().add("positive");
                } else {
                    changeLabel.getStyleClass().remove("positive");
                    changeLabel.getStyleClass().add("negative");
                }

                // Update statistics
                System.out.println("*** Setting market cap label");
                marketCapLabel.setText(formatMarketCap(currentStock.getMarketCap()));

                System.out.println("*** Setting P/E ratio label");
                peRatioLabel.setText(String.format("%.2f", currentStock.getPeRatio()));

                System.out.println("*** Setting dividend yield label");
                dividendYieldLabel.setText(percentFormat.format(currentStock.getDividendYield() / 100));

                System.out.println("*** Setting volume label");
                volumeLabel.setText(NumberFormat.getIntegerInstance().format(currentStock.getVolume()));

                System.out.println("*** All UI components updated successfully");
            } catch (Exception e) {
                System.out.println("*** ERROR updating stock info: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    private void updateWatchlistButtonState() {
        boolean isInWatchlist = user.getWatchlist().containsStock(currentStock.getSymbol());

        if (isInWatchlist) {
            addToWatchlistButton.setText("Remove from Watchlist");
        } else {
            addToWatchlistButton.setText("Add to Watchlist");
        }
    }
    private String formatMarketCap(double marketCap) {
        if (marketCap >= 1_000_000_000_000L) {
            return String.format("$%.2fT", marketCap / 1_000_000_000_000.0);
        } else if (marketCap >= 1_000_000_000L) {
            return String.format("$%.2fB", marketCap / 1_000_000_000.0);
        } else if (marketCap >= 1_000_000L) {
            return String.format("$%.2fM", marketCap / 1_000_000.0);
        } else {
            return String.format("$%.2fK", marketCap / 1_000.0);
        }
    }
    private void loadPriceHistory() {
        System.out.println("*** loadPriceHistory called");

        Platform.runLater(() -> {
            try {
                // Clear existing data
                priceChart.getData().clear();

                // Create a series for the price data
                XYChart.Series<Number, Number> series = new XYChart.Series<>();

                // Generate random price data for demonstration
                // In a real app, this would come from an API
                generateRandomPriceData(series);

                // Add the series to the chart
                priceChart.getData().add(series);

                System.out.println("*** Price history chart updated successfully");

            } catch (Exception e) {
                System.out.println("*** ERROR loading price history: " + e.getMessage());
                e.printStackTrace();
                showErrorDialog("Error", "Failed to load price history", e.getMessage());
            }
        });
    }
    private void generateRandomPriceData(XYChart.Series<Number, Number> series) {
        //Generate random price data for demonstration
        Random random = new Random();
        double basePrice = currentStock.getPrice() - (random.nextDouble() * 10);

        for (int i = 0; i < 30; i++) {
            double price = basePrice + (random.nextDouble() * 20 - 10);
            if (price < 0) price = 0.1; // Ensure price is positive
            series.getData().add(new XYChart.Data<>(i, price));
            basePrice = price; // Use the last price as the basis for the next one
        }
    }
    private void loadNewsItems() {
        System.out.println("*** loadNewsItems called");

        Platform.runLater(() -> {
            try {
                // Clear existing news items
                newsContainer.getChildren().clear();

                // Generate mock news items
                // In a real app, this would come from a news API
                addDummyNewsItems();

                System.out.println("*** News items updated successfully");
            } catch (Exception e) {
                System.out.println("*** ERROR loading news items: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    private void addDummyNewsItems() {
        addNewsItem("Quarterly Earnings Report", LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                currentStock.getName() + " reported strong Q3 earnings, exceeding analyst expectations.");
        addNewsItem("New Product Launch", LocalDate.now().minusWeeks(1).format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                currentStock.getName() + " announces innovative product line coming next quarter.");
        addNewsItem("Industry Analysis", LocalDate.now().minusWeeks(2).format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                "Experts predict growth in the sector where " + currentStock.getName() + " operates.");
    }

    private void addNewsItem(String title, String date, String content) {
        VBox newsItem = new VBox(5);
        newsItem.getStyleClass().add("news-item");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("news-title");

        Label dateLabel = new Label(date);
        dateLabel.getStyleClass().add("news-date");

        Label contentLabel = new Label(content);
        contentLabel.getStyleClass().add("news-content");
        contentLabel.setWrapText(true);

        newsItem.getChildren().addAll(titleLabel, dateLabel, contentLabel);
        newsContainer.getChildren().add(newsItem);
    }
    @FXML
    private void handleRefresh() {
        loadStockData(stockSymbol.get());
    }
    @FXML
    private void handleBuyShares() {
        try {
            if (sharesTextField.getText().isEmpty()) {
                showErrorDialog("Error", "Invalid quantity", "Please enter a valid number of shares.");
                return;
            }

            int shares = Integer.parseInt(sharesTextField.getText());
            if (shares <= 0) {
                showErrorDialog("Error", "Invalid quantity", "Please enter a positive number of shares.");
                return;
            }

            double totalCost = shares * currentStock.getPrice();
            if (totalCost > user.getBalance()) {
                showErrorDialog("Insufficient Funds",
                        "You don't have enough funds to complete this purchase.",
                        String.format("Cost: $%.2f, Balance: $%.2f", totalCost, user.getBalance()));
                return;
            }
            // Execute the purchase
            // Withdraw money first
            boolean success = user.withdraw(totalCost);

            if (success) {
                // Add to portfolio
                if (user.getPortfolio().containsStock(currentStock.getSymbol())) {
                    OwnedStock existingStock = user.getPortfolio().getStock(currentStock.getSymbol());
                    existingStock.addShares(shares, currentStock.getPrice());
                } else {
                    OwnedStock ownedStock = new OwnedStock(
                            currentStock.getSymbol(),
                            currentStock.getName(),
                            currentStock.getPrice(),
                            shares,
                            currentStock.getPrice()
                    );
                    user.getPortfolio().addStock(ownedStock);
                }

                // Add transaction record
                BuyTransaction transaction = new BuyTransaction(
                        currentStock.getSymbol(),
                        shares,
                        currentStock.getPrice()
                );
                user.addTransaction(transaction);

                showInfoDialog("Purchase Complete",
                        "Stock purchase successful",
                        String.format("You purchased %d shares of %s for $%.2f",
                                shares, currentStock.getSymbol(), totalCost));

                // Clear the text field
                sharesTextField.clear();

                // Update the user info in the main view
                updateUserInfo();
            } else {
                showErrorDialog("Purchase Failed",
                        "Failed to complete the purchase",
                        "There was an error processing your transaction.");
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Error", "Invalid quantity", "Please enter a valid number of shares.");
            sharesTextField.setText("");
        }
    }
    @FXML
    private void handleAddToWatchlist() {
        boolean isInWatchlist = user.getWatchlist().containsStock(currentStock.getSymbol());

        if (isInWatchlist) {
            // Remove from watchlist
            user.getWatchlist().removeStock(currentStock.getSymbol());
            addToWatchlistButton.setText("Add to Watchlist");
            showInfoDialog("Watchlist Updated",
                    "Stock removed from watchlist",
                    currentStock.getSymbol() + " has been removed from your watchlist.");
        } else {
            // Add to watchlist
            WatchlistStock watchlistStock = new WatchlistStock(
                    currentStock.getSymbol(),
                    currentStock.getName(),
                    currentStock.getPrice(),
                    currentStock.getChangePercent());

            user.getWatchlist().addStock(watchlistStock);
            addToWatchlistButton.setText("Remove from Watchlist");
            showInfoDialog("Watchlist Updated",
                    "Stock added to watchlist",
                    currentStock.getSymbol() + " has been added to your watchlist.");
        }

        // Update the user info in the main view
        updateUserInfo();
    }