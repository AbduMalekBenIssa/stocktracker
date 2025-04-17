package stocktracker;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import stocktracker.analysis.MarketAnalyzer;
import stocktracker.model.WatchlistStock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Market view
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class MarketViewController extends BaseController {

    @FXML
    private TableView<MarketStock> gainersTable;

    @FXML
    private TableView<MarketStock> losersTable;

    @FXML
    private TableView<MarketStock> activeTable;

    @FXML
    private TableColumn<MarketStock, String> gainerSymbolColumn;

    @FXML
    private TableColumn<MarketStock, String> gainerNameColumn;

    @FXML
    private TableColumn<MarketStock, Number> gainerPriceColumn;

    @FXML
    private TableColumn<MarketStock, Number> gainerChangeColumn;

    @FXML
    private TableColumn<MarketStock, String> loserSymbolColumn;

    @FXML
    private TableColumn<MarketStock, String> loserNameColumn;

    @FXML
    private TableColumn<MarketStock, Number> loserPriceColumn;

    @FXML
    private TableColumn<MarketStock, Number> loserChangeColumn;

    @FXML
    private TableColumn<MarketStock, String> activeSymbolColumn;

    @FXML
    private TableColumn<MarketStock, String> activeNameColumn;

    @FXML
    private TableColumn<MarketStock, Number> activePriceColumn;

    @FXML
    private VBox marketMovementsContainer;

    private MarketAnalyzer marketAnalyzer;
    private ObservableList<MarketStock> gainersData;
    private ObservableList<MarketStock> losersData;
    private ObservableList<MarketStock> activeData;

    /**
     * Helper class to store market stock data for tables
     */
    public static class MarketStock {
        private final String symbol;
        private final String name;
        private final double price;
        private final double change;

        public MarketStock(String symbol, String name, double price, double change) {
            this.symbol = symbol;
            this.name = name;
            this.price = price;
            this.change = change;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public double getChange() {
            return change;
        }
    }

    @Override
    protected void onInitialize() {
        this.marketAnalyzer = new MarketAnalyzer(stockMarket);

        setupTables();
        loadMarketData();
    }

    /**
     * Sets up the table columns and data binding
     */
    private void setupTables() {
        // Initialize data lists
        gainersData = FXCollections.observableArrayList();
        losersData = FXCollections.observableArrayList();
        activeData = FXCollections.observableArrayList();

        // Set data sources
        gainersTable.setItems(gainersData);
        losersTable.setItems(losersData);
        activeTable.setItems(activeData);

        // Setup gainers table columns
        setupTableColumns(
                gainerSymbolColumn, gainerNameColumn, gainerPriceColumn, gainerChangeColumn, true);

        // Setup losers table columns
        setupTableColumns(
                loserSymbolColumn, loserNameColumn, loserPriceColumn, loserChangeColumn, false);

        // Setup active table columns
        setupActiveTableColumns(
                activeSymbolColumn, activeNameColumn, activePriceColumn);

        // Set row factories for context menus
        setupRowFactory(gainersTable);
        setupRowFactory(losersTable);
        setupRowFactory(activeTable);
    }

    /**
     * Sets up the standard table columns (symbol, name, price, change)
     *
     * @param symbolColumn The symbol column
     * @param nameColumn The name column
     * @param priceColumn The price column
     * @param changeColumn The change column
     * @param isPositiveChange Whether the change is expected to be positive
     */
    private void setupTableColumns(
            TableColumn<MarketStock, String> symbolColumn,
            TableColumn<MarketStock, String> nameColumn,
            TableColumn<MarketStock, Number> priceColumn,
            TableColumn<MarketStock, Number> changeColumn,
            boolean isPositiveChange) {

        // Symbol column
        symbolColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSymbol()));

        // Name column
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        // Price column with currency formatting
        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()));
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", value.doubleValue()));
                }
            }
        });

        // Change column with formatting and colors
        changeColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getChange()));
        changeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    double change = value.doubleValue();
                    setText(String.format("%.2f%%", change));

                    if (change > 0) {
                        setStyle("-fx-text-fill: -fx-success;");
                        setText("+" + getText());
                    } else if (change < 0) {
                        setStyle("-fx-text-fill: -fx-danger;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    /**
     * Sets up the active stocks table columns (no change column)
     *
     * @param symbolColumn The symbol column
     * @param nameColumn The name column
     * @param priceColumn The price column
     */
    private void setupActiveTableColumns(
            TableColumn<MarketStock, String> symbolColumn,
            TableColumn<MarketStock, String> nameColumn,
            TableColumn<MarketStock, Number> priceColumn) {

        // Symbol column
        symbolColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSymbol()));

        // Name column
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        // Price column with currency formatting
        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()));
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", value.doubleValue()));
                }
            }
        });
    }

    /**
     * Sets up the row factory for a table to handle context menus and double clicks
     *
     * @param table The table to set up
     */
    private void setupRowFactory(TableView<MarketStock> table) {
        table.setRowFactory(tv -> {
            TableRow<MarketStock> row = new TableRow<>();

            // Handle double click
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showStockDetails(row.getItem());
                }
            });

            // Create context menu
            ContextMenu contextMenu = new ContextMenu();

            MenuItem addToWatchlistItem = new MenuItem("Add to Watchlist");
            addToWatchlistItem.setOnAction(e -> addToWatchlist(row.getItem()));

            MenuItem buyStockItem = new MenuItem("Buy Stock");
            buyStockItem.setOnAction(e -> buyStock(row.getItem()));

            MenuItem viewDetailsItem = new MenuItem("View Details");
            viewDetailsItem.setOnAction(e -> showStockDetails(row.getItem()));

            contextMenu.getItems().addAll(addToWatchlistItem, buyStockItem, new SeparatorMenuItem(), viewDetailsItem);

            // Only display for non-empty rows
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }

    /**
     * Loads market data from the API
     */
    private void loadMarketData() {
        try {
            // Clear existing data
            gainersData.clear();
            losersData.clear();
            activeData.clear();
            marketMovementsContainer.getChildren().clear();

            // Get market data
            loadTopGainers();
            loadTopLosers();
            loadMostActive();
            showMarketMovements();

        } catch (IOException e) {
            showErrorDialog("Market Data Error", "Failed to load market data", e.getMessage());
        }
    }

    /**
     * Loads the top gainers data
     */
    private void loadTopGainers() throws IOException {
        List<String> gainers = stockMarket.getTopGainers();

        for (String symbol : gainers) {
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);

                gainersData.add(new MarketStock(symbol, name, price, change));
            } catch (IOException e) {
                // Skip this stock if there's an error
                System.out.println("Error loading gainer: " + symbol + " - " + e.getMessage());
            }
        }
    }

    /**
     * Loads the top losers data
     */
    private void loadTopLosers() throws IOException {
        List<String> losers = stockMarket.getTopLosers();

        for (String symbol : losers) {
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);

                losersData.add(new MarketStock(symbol, name, price, change));
            } catch (IOException e) {
                // Skip this stock if there's an error
                System.out.println("Error loading loser: " + symbol + " - " + e.getMessage());
            }
        }
    }

    /**
     * Loads the most actively traded stocks
     */
    private void loadMostActive() throws IOException {
        List<String> active = stockMarket.getMostActivelyTraded();

        for (String symbol : active) {
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);

                activeData.add(new MarketStock(symbol, name, price, change));
            } catch (IOException e) {
                // Skip this stock if there's an error
                System.out.println("Error loading active stock: " + symbol + " - " + e.getMessage());
            }
        }
    }


    /**
     * Shows unusual market movements
     */
    private void showMarketMovements() throws IOException {
        // Call the market analyzer to check for unusual movements
        List<String> movements = getUnusualMovements();

        if (movements.isEmpty()) {
            Label noMovementsLabel = new Label("No unusual market movements detected today.");
            noMovementsLabel.getStyleClass().add("text-secondary");
            marketMovementsContainer.getChildren().add(noMovementsLabel);
        } else {
            for (String movement : movements) {
                Label movementLabel = new Label(movement);
                movementLabel.getStyleClass().add("market-movement-item");
                marketMovementsContainer.getChildren().add(movementLabel);
            }
        }
    }

    /**
     * Gets a list of unusual market movements
     *
     * @return A list of unusual market movement descriptions
     */
    private List<String> getUnusualMovements() throws IOException {
        List<String> result = new ArrayList<>();

        // Combine gainers and losers
        List<MarketStock> allStocks = new ArrayList<>();
        allStocks.addAll(gainersData);
        allStocks.addAll(losersData);

        // Find stocks with extreme movements
        for (MarketStock stock : allStocks) {
            double change = stock.getChange();
            if (Math.abs(change) > 10.0) { // Consider >10% change unusual
                String direction = change > 0 ? "up" : "down";
                result.add(String.format("%s (%s) is %s %.2f%% to $%.2f",
                        stock.getName(), stock.getSymbol(), direction, Math.abs(change), stock.getPrice()));
            }
        }

        return result;
    }

    /**
     * Refreshes the market data
     */
    @FXML
    private void refreshMarketData() {
        loadMarketData();
        showInfoDialog("Refresh Complete", "Market Data Updated", "Market data has been refreshed with the latest information.");
    }

    /**
     * Shows detailed information for a stock
     *
     * @param stock The stock to show details for
     */
    private void showStockDetails(MarketStock stock) {
        // Show the detailed stock view
        mainController.showStockDetails(stock.getSymbol());
    }

    /**
     * Adds a stock to the watchlist
     *
     * @param stock The stock to add to the watchlist
     */
    private void addToWatchlist(MarketStock stock) {
        try {
            // Check if the stock is already in the watchlist
            if (user.getWatchlist().containsStock(stock.getSymbol())) {
                showErrorDialog("Already in Watchlist", "Stock Already in Watchlist",
                        "The stock " + stock.getSymbol() + " is already in your watchlist.");
                return;
            }

            // Add to watchlist
            WatchlistStock watchlistStock = new WatchlistStock(
                    stock.getSymbol(), stock.getName(), stock.getPrice(), stock.getChange());
            user.getWatchlist().addStock(watchlistStock);

            showInfoDialog("Stock Added", "Stock Added to Watchlist",
                    String.format("Successfully added %s (%s) to your watchlist.",
                            stock.getName(), stock.getSymbol()));

        } catch (Exception e) {
            showErrorDialog("Error", "Could not add stock to watchlist", e.getMessage());
        }
    }

    /**
     * Shows a dialog to buy a stock
     *
     * @param stock The stock to buy
     */
    private void buyStock(MarketStock stock) {
        try {
            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Buy Stock");
            dialog.setHeaderText("Buy " + stock.getName() + " (" + stock.getSymbol() + ")");

            // Set the button types
            ButtonType buyButtonType = new ButtonType("Buy", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(buyButtonType, ButtonType.CANCEL);

            // Create the form fields
            VBox content = new VBox(10);
            content.getStyleClass().add("dialog-content");

            Label priceLabel = new Label(String.format("Current Price: $%.2f", stock.getPrice()));
            Label balanceLabel = new Label(String.format("Available Balance: $%.2f", user.getBalance()));

            TextField quantityField = new TextField();
            quantityField.setPromptText("Number of Shares");

            content.getChildren().addAll(
                    priceLabel,
                    new Label("Quantity:"),
                    quantityField,
                    balanceLabel
            );

            dialog.getDialogPane().setContent(content);

            // Request focus on the quantity field by default
            quantityField.requestFocus();

            // Show the dialog and process the result
            java.util.Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == buyButtonType) {
                try {
                    // Get the quantity from the form
                    int quantity = Integer.parseInt(quantityField.getText());

                    // Validate the input
                    if (quantity <= 0) {
                        showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a positive number for the quantity.");
                        return;
                    }

                    double price = stock.getPrice();

                    // Calculate total cost
                    double totalCost = price * quantity;

                    // Check if user has enough funds
                    if (totalCost > user.getBalance()) {
                        showErrorDialog("Insufficient Funds", "Insufficient Funds",
                                String.format("You don't have enough funds to buy %d shares of %s for $%.2f.",
                                        quantity, stock.getSymbol(), totalCost));
                        return;
                    }

                    // If the user already owns this stock, handle accordingly
                    if (user.getPortfolio().containsStock(stock.getSymbol())) {
                        user.getPortfolio().getStock(stock.getSymbol()).addShares(quantity, price);
                    } else {
                        // Create a new OwnedStock and add it to the portfolio
                        stocktracker.model.OwnedStock newStock = new stocktracker.model.OwnedStock(
                                stock.getSymbol(), stock.getName(), price, quantity, price);
                        user.getPortfolio().addStock(newStock);
                    }

                    // Withdraw the funds
                    user.withdraw(totalCost);

                    // Add a transaction record
                    user.addTransaction(new stocktracker.model.BuyTransaction(
                            stock.getSymbol(), quantity, price, java.time.LocalDateTime.now()));

                    // Update the main view's user info
                    updateUserInfo();

                    showInfoDialog("Purchase Complete", "Stock Purchased",
                            String.format("Successfully purchased %d shares of %s (%s) for $%.2f.",
                                    quantity, stock.getName(), stock.getSymbol(), totalCost));

                } catch (NumberFormatException e) {
                    showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a valid number for the quantity.");
                }
            }
        } catch (Exception e) {
            showErrorDialog("Error", "Could not complete purchase", e.getMessage());
        }
    }

}
