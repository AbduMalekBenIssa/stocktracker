package stocktracker;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import stocktracker.model.OwnedStock;
import stocktracker.model.WatchlistStock;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the Watchlist view
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class WatchlistController extends BaseController {
    @FXML
    private Label watchlistCountLabel;

    @FXML
    private TableView<WatchlistStock> watchlistTable;

    @FXML
    private TableColumn<WatchlistStock, String> symbolColumn;

    @FXML
    private TableColumn<WatchlistStock, String> nameColumn;

    @FXML
    private TableColumn<WatchlistStock, Number> priceColumn;

    @FXML
    private TableColumn<WatchlistStock, Number> changeColumn;

    private ObservableList<WatchlistStock> stockList;

    @Override
    protected void onInitialize() {
        setupTable();
        updateWatchlistData();
    }
    /**
     * Sets up the table columns and cell factories
     */
    private void setupTable() {
        // Initialize the observable list
        stockList = FXCollections.observableArrayList();
        watchlistTable.setItems(stockList);

        // Setup the columns
        symbolColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSymbol()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        // Format currency values
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()));
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
        // Change percentage with colored text
        changeColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getChangePercentage()));
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
        // Set row factory for context menu
        watchlistTable.setRowFactory(tv -> {
            TableRow<WatchlistStock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showStockDetailsDialog(row.getItem());
                }
            });

            // Create context menu
            ContextMenu contextMenu = new ContextMenu();

            MenuItem buyMenuItem = new MenuItem("Buy Stock");
            buyMenuItem.setOnAction(e -> buyStock(row.getItem()));

            MenuItem removeMenuItem = new MenuItem("Remove from Watchlist");
            removeMenuItem.setOnAction(e -> removeFromWatchlist(row.getItem()));

            MenuItem detailsMenuItem = new MenuItem("View Details");
            detailsMenuItem.setOnAction(e -> showStockDetailsDialog(row.getItem()));

            contextMenu.getItems().addAll(buyMenuItem, new SeparatorMenuItem(), removeMenuItem, new SeparatorMenuItem(), detailsMenuItem);

            // Only display context menu for non-empty rows
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }
    /**
     * Updates the watchlist data
     */
    private void updateWatchlistData() {
        // Clear the list
        stockList.clear();

        // Add all stocks from the watchlist
        stockList.addAll(user.getWatchlist().getAllStocks());

        // Update summary label
        int stockCount = stockList.size();
        watchlistCountLabel.setText(String.valueOf(stockCount));
    }
    /**
     * Refreshes the stock prices in the watchlist
     */
    @FXML
    private void refreshPrices() {
        try {
            // Update each stock price
            for (WatchlistStock stock : stockList) {
                double price = stockMarket.getStockPrice(stock.getSymbol());
                double change = stockMarket.getDailyChangePercentage(stock.getSymbol());
                stock.update(price, change);
            }

            // Refresh the table
            watchlistTable.refresh();

            showInfoDialog("Refresh Complete", "Prices Updated", "Stock prices have been updated with the latest market data.");
        } catch (IOException e) {
            showErrorDialog("Refresh Error", "Could not update stock prices", e.getMessage());
        }
    }
    /**
     * Shows a dialog to add a new stock to the watchlist
     */
    @FXML
    private void addToWatchlist() {
        // Create the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add to Watchlist");
        dialog.setHeaderText("Add a Stock to Your Watchlist");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the form fields
        VBox content = new VBox(10);
        content.getStyleClass().add("dialog-content");

        TextField symbolField = new TextField();
        symbolField.setPromptText("Stock Symbol (e.g., AAPL)");

        Label symbolInfoLabel = new Label("Enter a valid stock symbol");

        content.getChildren().addAll(
                new Label("Stock Symbol:"),
                symbolField,
                symbolInfoLabel
        );
        // Add validation for the symbol
        symbolField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String symbol = newValue.toUpperCase();
                    if (stockMarket.isValidSymbol(symbol)) {
                        String name = stockMarket.getCompanyName(symbol);
                        double price = stockMarket.getStockPrice(symbol);
                        symbolInfoLabel.setText(String.format("%s - Current Price: $%.2f", name, price));
                        symbolInfoLabel.getStyleClass().removeAll("text-danger");
                        symbolInfoLabel.getStyleClass().add("text-info");
                    } else {
                        symbolInfoLabel.setText("Invalid stock symbol");
                        symbolInfoLabel.getStyleClass().removeAll("text-info");
                        symbolInfoLabel.getStyleClass().add("text-danger");
                    }
                } catch (IOException e) {
                    symbolInfoLabel.setText("Error checking symbol: " + e.getMessage());
                    symbolInfoLabel.getStyleClass().removeAll("text-info");
                    symbolInfoLabel.getStyleClass().add("text-danger");
                }
            } else {
                symbolInfoLabel.setText("Enter a valid stock symbol");
                symbolInfoLabel.getStyleClass().removeAll("text-danger", "text-info");
            }
        });
        dialog.getDialogPane().setContent(content);

        // Request focus on the symbol field by default
        symbolField.requestFocus();

        // Show the dialog and process the result
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == addButtonType) {
            try {
                // Get the values from the form
                String symbol = symbolField.getText().toUpperCase();

                // Validate the input
                if (symbol.isEmpty()) {
                    showErrorDialog("Invalid Input", "Invalid Input", "Please enter a valid stock symbol.");
                    return;
                }

                // Check if the symbol is valid
                if (!stockMarket.isValidSymbol(symbol)) {
                    showErrorDialog("Invalid Symbol", "Invalid Stock Symbol", "The stock symbol you entered is not valid.");
                    return;
                }
                // Check if the stock is already in the watchlist
                if (user.getWatchlist().containsStock(symbol)) {
                    showErrorDialog("Duplicate Stock", "Stock Already in Watchlist",
                            "The stock " + symbol + " is already in your watchlist.");
                    return;
                }

                // Get stock info
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);

                // Add to watchlist
                WatchlistStock stock = new WatchlistStock(symbol, name, price, change);
                user.getWatchlist().addStock(stock);

                // Update the view
                updateWatchlistData();

                showInfoDialog("Stock Added", "Stock Added to Watchlist",
                        String.format("Successfully added %s (%s) to your watchlist.", name, symbol));

            } catch (IOException e) {
                showErrorDialog("API Error", "Could not add stock to watchlist",
                        "Error accessing stock market data: " + e.getMessage());
            }
        }
    }
    /**
     * Removes a stock from the watchlist
     *
     * @param stock The stock to remove
     */
    private void removeFromWatchlist(WatchlistStock stock) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove from Watchlist");
        alert.setHeaderText("Remove Stock from Watchlist");
        alert.setContentText(String.format("Are you sure you want to remove %s (%s) from your watchlist?",
                stock.getName(), stock.getSymbol()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            user.getWatchlist().removeStock(stock.getSymbol());
            updateWatchlistData();
            showInfoDialog("Stock Removed", "Stock Removed from Watchlist",
                    String.format("Successfully removed %s (%s) from your watchlist.",
                            stock.getName(), stock.getSymbol()));
        }
    }
    /**
     * Shows a dialog to buy a stock from the watchlist
     *
     * @param stock The stock to buy
     */
    private void buyStock(WatchlistStock stock) {
        try {
            // Get current price
            double price = stockMarket.getStockPrice(stock.getSymbol());

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

            Label priceLabel = new Label(String.format("Current Price: $%.2f", price));
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
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == buyButtonType) {
                try {
                    // Get the quantity from the form
                    int quantity = Integer.parseInt(quantityField.getText());

                    // Validate the input
                    if (quantity <= 0) {
                        showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a positive number for the quantity.");
                        return;
                    }

                    // Calculate total cost
                    double totalCost = price * quantity;

                    // Check if user has enough funds
                    if (totalCost > user.getBalance()) {
                        showErrorDialog("Insufficient Funds", "Insufficient Funds",
                                String.format("You don't have enough funds to buy %d shares of %s for $%.2f.",
                                        quantity, stock.getSymbol(), totalCost));
                        return;
                    }
                    // If the user already owns this stock, we need to handle it accordingly
                    if (user.getPortfolio().containsStock(stock.getSymbol())) {
                        OwnedStock existingStock = user.getPortfolio().getStock(stock.getSymbol());
                        existingStock.addShares(quantity, price);
                    } else {
                        // Create a new OwnedStock instance and add it to the portfolio
                        OwnedStock newStock = new OwnedStock(stock.getSymbol(), stock.getName(),
                                price, quantity, price);
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
        } catch (IOException e) {
            showErrorDialog("API Error", "Could not complete purchase", "Error accessing stock market data: " + e.getMessage());
        }
    }
    /**
     * Shows detailed information for a stock
     *
     * @param stock The stock to show details for
     */
    private void showStockDetailsDialog(WatchlistStock stock) {
        // Show the detailed stock view
        mainController.showStockDetails(stock.getSymbol());
    }

    @Override
    public void refreshView() {
        updateWatchlistData();
    }
} 
}